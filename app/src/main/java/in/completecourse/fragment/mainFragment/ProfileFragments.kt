package `in`.completecourse.fragment.mainFragment

import `in`.completecourse.EditProfileActivity
import `in`.completecourse.R
import `in`.completecourse.activity.MoreAppsActivity
import `in`.completecourse.databinding.FragmentProfileBinding
import `in`.completecourse.helper.PrefManager
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragments : Fragment() {
    private var prefManager: PrefManager? = null

    private var _binding: FragmentProfileBinding? = null
    //This property is only valid between onCreateView and
    //onDestroyView
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (context != null) prefManager = PrefManager(context!!)

        if (FirebaseAuth.getInstance().uid != null)
            getUserProfile(FirebaseAuth.getInstance().uid!!)

        binding.btnLogout.setOnClickListener { logoutUser() }
        binding.btnEditProfile.setOnClickListener { editProfile() }
        binding.btnShare.setOnClickListener{ shareApp() }
        binding.btnMoreApp.setOnClickListener{ moreApps() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        FirebaseFirestore.getInstance().collection("users").document(userId).get()
            .addOnCompleteListener { binding.progressBar.visibility = View.GONE }
            .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
            binding.tvUserName.text = getString(R.string.username, documentSnapshot.getString("name"))
            if (documentSnapshot.getString("email") == null) binding.tvEmail.text = getString(R.string.email, "Not Provided")
            else binding.tvEmail.text = getString(R.string.email, documentSnapshot.getString("email"))
            if (documentSnapshot.getString("class") == null) binding.tvClass.text = getString(R.string.user_class, "Not Provided")
            else binding.tvClass.text = getString(R.string.user_class, documentSnapshot.getString("class"))
            if (documentSnapshot.getString("school") == null) binding.tvSchool.text = getString(R.string.school, "Not Provided")
            else binding.tvSchool.text = getString(R.string.school, documentSnapshot.getString("school"))
            if (documentSnapshot.getString("phone") == null) binding.tvContact.text = getString(R.string.contact, "Not Provided")
            else binding.tvContact.text = getString(R.string.contact, documentSnapshot.getString("phone"))
        }.addOnFailureListener { e: Exception -> Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show() }
    }
}