package `in`.completecourse.fragment.authFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.R
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.mukesh.OtpView
import java.util.*
import java.util.concurrent.TimeUnit

class OTPFragment : Fragment() {
    private var mAuth: FirebaseAuth? = null
    private var verificationId: String? = null
    private var progressBar: ProgressBar? = null
    private var otpView: OtpView? = null
    private var username: String? = null
    private var db: FirebaseFirestore? = null
    private var number: String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_otp_verify, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        otpView = view.findViewById(R.id.otp_view)
        progressBar = view.findViewById(R.id.progress_bar)
        val bundle = arguments
        if (bundle != null) {
            number = bundle.getString("phoneNumber")
            username = bundle.getString("edTvUsername")
            sendVerificationCode("+91$number") //Send OTP on phoneNumber
        }
    }

    private fun sendVerificationCode(number: String) {
        //Show Progress Bar
        progressBar!!.visibility = View.VISIBLE
        //[START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,  // Phone number to verify
                60,  // Timeout duration
                TimeUnit.SECONDS,  // Unit of timeout
                TaskExecutors.MAIN_THREAD,  // Activity (for callback binding)
                mCallBack // OnVerificationStateChangedCallbacks
        )
        //[END start_phone_auth]
    }

    private val mCallBack: OnVerificationStateChangedCallbacks = object : OnVerificationStateChangedCallbacks() {
        override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
            super.onCodeSent(s, forceResendingToken)
            //The SMS verification code has been sent to the provided phone number, we
            //now need to ask the user to enter the code and then construct a credential
            //by combining the code with a verification ID.
            //progressBar.setVisibility(View.GONE);
            verificationId = s
        }

        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            //This callback will be invoked in two situations
            // 1 - Instant verification:
            //     In some cases the phone number can be instantly verified.
            //     without needing to send or enter a verification code.
            // 2 - Auto-retrieval:
            //     On some devices Google Play Services can automatically detect
            //     the incoming verification SMS and perform verification without user action.
            val code = phoneAuthCredential.smsCode
            if (code != null) {
                progressBar!!.visibility = View.GONE
                otpView!!.setText(code)
                verifyCode(code)
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            //This callback is invoked if an invalid request for verification is made,
            //for instance if the phone number format is not valid.
            //progressBar.setVisibility(View.GONE);
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener { task: Task<AuthResult?>? -> progressBar!!.visibility = View.GONE }
                .addOnSuccessListener { authResult: AuthResult ->
                    if (context != null) {
                        if (authResult.user != null) {
                            progressBar!!.visibility = View.VISIBLE
                            db!!.collection("users").document(authResult.user!!.uid).get()
                                    .addOnCompleteListener { task: Task<DocumentSnapshot?>? -> progressBar!!.visibility = View.GONE }
                                    .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                                        if (documentSnapshot.exists()) {
                                            //User details are already in the database
                                            if (context != null) {
                                                val prefManager = PrefManager(context)
                                                prefManager.setFirstTimeLaunch(false)
                                                val intent = Intent(context, MainActivity::class.java)
                                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                startActivity(intent)
                                            }
                                        } else {
                                            progressBar!!.visibility = View.VISIBLE
                                            //User details not available in database save them
                                            val userDetails: MutableMap<String, String?> = HashMap()
                                            userDetails["name"] = username
                                            userDetails["phone"] = number
                                            if (authResult.user != null) userDetails["userid"] = authResult.user!!.uid
                                            db!!.collection("users").document(authResult.user!!.uid).set(userDetails)
                                                    .addOnCompleteListener { task: Task<Void?>? -> progressBar!!.visibility = View.GONE }.addOnSuccessListener { aVoid: Void? ->
                                                        if (context != null) {
                                                            val prefManager = PrefManager(context)
                                                            prefManager.setFirstTimeLaunch(false)
                                                            val intent = Intent(context, MainActivity::class.java)
                                                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                            startActivity(intent)
                                                        }
                                                    }.addOnFailureListener { e: Exception -> Toast.makeText(context, e.message, Toast.LENGTH_LONG).show() }
                                        }
                                    }.addOnFailureListener { e: Exception -> Toast.makeText(context, e.message, Toast.LENGTH_LONG).show() }
                        }
                    }
                }
    }
}