package `in`.completecourse.fragment.mainFragment

import `in`.completecourse.EditProfileActivity
import `in`.completecourse.R
import `in`.completecourse.activity.MoreAppsActivity
import `in`.completecourse.helper.PrefManager
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragments : Fragment() {
    private var prefManager: PrefManager? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (context != null) prefManager = PrefManager(context!!)

        if (FirebaseAuth.getInstance().uid != null)
            getUserProfile(FirebaseAuth.getInstance().uid!!)

        btn_logout.setOnClickListener { logoutUser() }
        btn_edit_profile.setOnClickListener { editProfile() }
        btn_share.setOnClickListener{ shareApp() }
        btn_more_app.setOnClickListener{ moreApps() }
    }


    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
        if (context != null) prefManager?.logoutUser(context!!)
    }

    private fun editProfile() {
        val intent = Intent(context, EditProfileActivity::class.java)
        startActivity(intent)
    }

    private fun shareApp() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        val shareBody = "Hey download Complete Course App and learn on the go.\n https://play.google.com/store/apps/details?id=in.completecourse&hl=en_IN"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Download App")
        intent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(intent, "Share via"))
    }

    private fun moreApps() {
        val intent = Intent(context, MoreAppsActivity::class.java)
        startActivity(intent)
    }

    private fun getUserProfile(userId: String) {
        FirebaseFirestore.getInstance().collection("users").document(userId).get().addOnCompleteListener { task: Task<DocumentSnapshot?>? -> progressBar!!.visibility = View.GONE }.addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
            tv_userName.text = getString(R.string.username, documentSnapshot.getString("name"))
            if (documentSnapshot.getString("email") == null) tv_email.text = getString(R.string.email, "Not Provided")
            else tv_email.text = getString(R.string.email, documentSnapshot.getString("email"))
            if (documentSnapshot.getString("class") == null) tv_class.text = getString(R.string.user_class, "Not Provided")
            else tv_class.text = getString(R.string.user_class, documentSnapshot.getString("class"))
            if (documentSnapshot.getString("school") == null) tv_school.text = getString(R.string.school, "Not Provided")
            else tv_school.text = getString(R.string.school, documentSnapshot.getString("school"))
            if (documentSnapshot.getString("phone") == null) tv_contact.text = getString(R.string.contact, "Not Provided")
            else tv_contact.text = getString(R.string.contact, documentSnapshot.getString("phone"))
        }.addOnFailureListener { e: Exception -> Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show() }
    }
}