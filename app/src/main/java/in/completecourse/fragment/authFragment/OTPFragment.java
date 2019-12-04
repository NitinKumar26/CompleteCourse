package in.completecourse.fragment.authFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mukesh.OtpView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import in.completecourse.MainActivity;
import in.completecourse.R;
import in.completecourse.helper.PrefManager;

public class OTPFragment extends Fragment {
    private FirebaseAuth mAuth;
    private String verificationId;
    private ProgressBar progressBar;
    private OtpView otpView;
    private String username;
    private FirebaseFirestore db;
    private String number;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_otp_verify, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        otpView = view.findViewById(R.id.otp_view);
        progressBar = view.findViewById(R.id.progress_bar);
        Bundle bundle = getArguments();
        if (bundle != null) {
            number = bundle.getString("phoneNumber");
            username = bundle.getString("edTvUsername");
            sendVerificationCode("+91" + number); //Send OTP on phoneNumber
        }
    }

    private void sendVerificationCode(String number) {
        //Show Progress Bar
        progressBar.setVisibility(View.VISIBLE);
        //[START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,                       // Phone number to verify
                60,                        // Timeout duration
                TimeUnit.SECONDS,             // Unit of timeout
                TaskExecutors.MAIN_THREAD,    // Activity (for callback binding)
                mCallBack                     // OnVerificationStateChangedCallbacks
        );
        //[END start_phone_auth]
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            //The SMS verification code has been sent to the provided phone number, we
            //now need to ask the user to enter the code and then construct a credential
            //by combining the code with a verification ID.
            //progressBar.setVisibility(View.GONE);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //This callback will be invoked in two situations
            // 1 - Instant verification:
            //     In some cases the phone number can be instantly verified.
            //     without needing to send or enter a verification code.
            // 2 - Auto-retrieval:
            //     On some devices Google Play Services can automatically detect
            //     the incoming verification SMS and perform verification without user action.

            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                progressBar.setVisibility(View.GONE);
                otpView.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            //This callback is invoked if an invalid request for verification is made,
            //for instance if the phone number format is not valid.
            //progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task ->
                        progressBar.setVisibility(View.GONE))
                .addOnSuccessListener(authResult -> {
                    if (getContext() != null) {
                        if (authResult.getUser() != null) {
                            progressBar.setVisibility(View.VISIBLE);
                            db.collection("users").document(authResult.getUser().getUid()).get()
                                    .addOnCompleteListener(task -> progressBar.setVisibility(View.GONE))
                                    .addOnSuccessListener(documentSnapshot -> {
                                        if (documentSnapshot.exists()){
                                            //User details are already in the database
                                            if (getContext() != null) {
                                                PrefManager prefManager = new PrefManager(getContext());
                                                prefManager.setFirstTimeLaunch(false);
                                                Intent intent = new Intent(getContext(), MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                        }else{
                                            progressBar.setVisibility(View.VISIBLE);
                                            //User details not available in database save them
                                            Map<String, String> userDetails = new HashMap<>();
                                            userDetails.put("name", username);
                                            userDetails.put("phone", number);
                                            if (authResult.getUser() != null)
                                                userDetails.put("userid", authResult.getUser().getUid());
                                            db.collection("users").document(authResult.getUser().getUid()).set(userDetails)
                                                    .addOnCompleteListener(task -> progressBar.setVisibility(View.GONE)).addOnSuccessListener(aVoid -> {
                                                if (getContext() != null) {
                                                    PrefManager prefManager = new PrefManager(getContext());
                                                    prefManager.setFirstTimeLaunch(false);
                                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                }
                                            }).addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show());
                                        }
                                    }).addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show());
                        }
                    }
                });
    }
}
