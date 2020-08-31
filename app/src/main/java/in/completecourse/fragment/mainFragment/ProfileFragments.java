package in.completecourse.fragment.mainFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.completecourse.EditProfileActivity;
import in.completecourse.R;
import in.completecourse.activity.MoreAppsActivity;
import in.completecourse.helper.PrefManager;

public class ProfileFragments extends Fragment {
    @BindView(R.id.tv_userName)
    TextView tvUserName;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_class)
    TextView tvClass;
    @BindView(R.id.tv_school)
    TextView tvSchool;
    @BindView(R.id.tv_contact)
    TextView tvContact;
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private FirebaseFirestore db;
    private PrefManager prefManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (getContext() != null) prefManager = new PrefManager(getContext());
        //userId = prefManager.getUserId();

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        getUserProfile(mAuth.getUid());

    }

    @OnClick(R.id.btn_logout)
    void logoutUser(){
        FirebaseAuth.getInstance().signOut();
        if (getContext() != null) prefManager.logoutUser(getContext());
    }

    @OnClick(R.id.btn_edit_profile)
    void editProfile(){
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_share)
    void shareApp(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBody = "Hey download Complete Course App and learn on the go.\n https://play.google.com/store/apps/details?id=in.completecourse&hl=en_IN";
        intent.putExtra(Intent.EXTRA_SUBJECT, "Download App");
        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(intent, "Share via"));
    }

    @OnClick(R.id.btn_more_app)
    void moreApps(){
        Intent intent = new Intent(getContext(), MoreAppsActivity.class);
        startActivity(intent);
    }

    private void getUserProfile(String userId){
        db.collection("users").document(userId).get().addOnCompleteListener(task -> progressBar.setVisibility(View.GONE)).addOnSuccessListener(documentSnapshot -> {

            tvUserName.setText(getString(R.string.username, documentSnapshot.getString("name")));

            if (documentSnapshot.getString("email") == null) tvEmail.setText(getString(R.string.email, "Not Provided"));
            else tvEmail.setText(getString(R.string.email, documentSnapshot.getString("email")));

            if (documentSnapshot.getString("class") == null) tvClass.setText(getString(R.string.user_class, "Not Provided"));
            else tvClass.setText(getString(R.string.user_class, documentSnapshot.getString("class")));

            if (documentSnapshot.getString("school") == null) tvSchool.setText(getString(R.string.school, "Not Provided"));
            else tvSchool.setText(getString(R.string.school, documentSnapshot.getString("school")));

            if (documentSnapshot.getString("phone") == null) tvContact.setText(getString(R.string.contact, "Not Provided"));
            else tvContact.setText(getString(R.string.contact, documentSnapshot.getString("phone")));

        }).addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());

    }

}

