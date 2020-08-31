package in.completecourse;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import com.google.ads.mediation.facebook.FacebookMediationAdapter;
//import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
//import com.google.android.gms.ads.mediation.InitializationCompleteCallback;
import com.adcolony.sdk.AdColony;
import com.google.ads.mediation.adcolony.AdColonyMediationAdapter;
import com.google.android.gms.ads.mediation.InitializationCompleteCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.completecourse.adapter.SpinAdapterNew;

public class EditProfileActivity extends AppCompatActivity {
    @BindView(R.id.classSelection)
    Spinner spinner;
    @BindView(R.id.edTv_username)
    EditText edTvUsername;
    @BindView(R.id.edTv_school)
    EditText edTvSchool;
    @BindView(R.id.edTv_email)
    EditText edTvEmail;
    @BindView(R.id.edTv_contact)
    EditText edTvContact;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ButterKnife.bind(this);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String[] userClassArray = new String[]{"Choose Class", "9", "10", "11", "12"};
        SpinAdapterNew spinAdapter = new SpinAdapterNew(this, R.layout.spinner_row, userClassArray);

        spinner.setAdapter(spinAdapter);

        getUserProfile(mAuth.getUid());

        /*
        AdColonyMediationAdapter adColonyMediationAdapter = new AdColonyMediationAdapter();
        adColonyMediationAdapter.initialize(this, new InitializationCompleteCallback() {
            @Override
            public void onInitializationSucceeded() {

            }

            @Override
            public void onInitializationFailed(String s) {

            }
        }, AdColony.configure(this, appOptions, getString(R.string.ad_colony_app_id), getString(R.string.ad_colony_interstitial_ad_zone)));

         */


    }
    
    @OnClick(R.id.btn_done)
    void done(){
        String username; 
        String userClass = null; 
        String userEmail; 
        String userSchool;
        String contact;
        
        username = edTvUsername.getText().toString();
        userEmail = edTvEmail.getText().toString(); 
        userSchool = edTvSchool.getText().toString();
        contact = edTvContact.getText().toString();
        
        if (userEmail.equals("Not Provided")) userEmail = "";
        if (userSchool.equals("Not Provided")) userSchool = "";
        if (contact.equals("Not Provided")) contact = "";
        
        if (spinner.getSelectedItemPosition() == 0) userClass = "";
        else if (spinner.getSelectedItemPosition() == 1) userClass = "9th"; 
        else if (spinner.getSelectedItemPosition() == 2) userClass = "10th"; 
        else if (spinner.getSelectedItemPosition() == 3) userClass = "11th"; 
        else if (spinner.getSelectedItemPosition() == 4) userClass = "12th"; 
        if (userClass != null) {
            if (!username.isEmpty() && !userEmail.isEmpty() && !userClass.isEmpty() &&
                    !userSchool.isEmpty() && !contact.isEmpty()) {
                updateProfile(username, userClass, userEmail, userSchool, contact);
            } else Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show();
        }
    }

    private void getUserProfile(String userId){
        db.collection("users").document(userId).get().addOnCompleteListener(task -> progressBar.setVisibility(View.GONE)).addOnSuccessListener(documentSnapshot -> {
            
            edTvUsername.setText(documentSnapshot.getString("name"));

            if (documentSnapshot.getString("email") == null) edTvEmail.setText("Not Provided");
            else edTvEmail.setText(documentSnapshot.getString("email"));

            String userClass = documentSnapshot.getString("class");
            if (userClass == null) spinner.setSelection(0);
            else if (userClass.equals("9th")) spinner.setSelection(1);
            else if (userClass.equals("10th")) spinner.setSelection(2);
            else if (userClass.equals("11th")) spinner.setSelection(3);
            else if (userClass.equals("12th")) spinner.setSelection(4);

            if (documentSnapshot.getString("school") == null) edTvSchool.setText("Not Provided");
            else edTvSchool.setText(documentSnapshot.getString("school"));

            if (documentSnapshot.getString("phone") == null) edTvContact.setText("--");
            else edTvContact.setText(documentSnapshot.getString("phone"));

        }).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());

    }

    private void updateProfile(String username, String userClass, String userEmail, String userSchool, String contact) {
        progressBar.setVisibility(View.VISIBLE);
        Map<String, Object> data = new HashMap<>();
        data.put("name", username);
        data.put("class", userClass);
        data.put("email", userEmail);
        data.put("school", userSchool);
        data.put("phone", contact);
        if (mAuth.getUid() != null) {
            db.collection("users")
                    .document(mAuth.getUid()).set(data)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show())
                    .addOnCompleteListener(task -> progressBar.setVisibility(View.GONE));
        }

    }
}
