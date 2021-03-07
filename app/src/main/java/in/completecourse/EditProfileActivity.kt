package `in`.completecourse

import `in`.completecourse.adapter.SpinAdapterNew
import `in`.completecourse.databinding.ActivityEditProfileBinding
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val userClassArray = arrayOf("Choose Class", "9", "10", "11", "12")

        val spinAdapter = SpinAdapterNew(this, R.layout.spinner_row, userClassArray)

        binding.classSelection.adapter = spinAdapter

        getUserProfile(FirebaseAuth.getInstance().uid)

        binding.btnDone.setOnClickListener{ done() }
    }

    private fun done() {
        var userClass: String? = null
        var userEmail: String
        var userSchool: String
        var contact: String
        val username: String = binding.edTvUsername.text.toString()
        userEmail = binding.edTvEmail.text.toString()
        userSchool = binding.edTvSchool.text.toString()
        contact = binding.edTvContact.text.toString()
        if (userEmail == "Not Provided") userEmail = ""
        if (userSchool == "Not Provided") userSchool = ""
        if (contact == "Not Provided") contact = ""

        when (binding.classSelection.selectedItemPosition) {
            0 -> userClass = ""
            1 -> userClass = "9th"
            2 -> userClass = "10th"
            3 -> userClass = "11th"
            4 -> userClass = "12th"
        }
        if (userClass != null) {
            if (username.isNotEmpty() && userEmail.isNotEmpty() && userClass.isNotEmpty() &&
                    userSchool.isNotEmpty() && contact.isNotEmpty()) {
                updateProfile(username, userClass, userEmail, userSchool, contact)
            } else Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUserProfile(userId: String?) {
        FirebaseFirestore.getInstance().collection("users").document(userId!!).get()
                .addOnCompleteListener { binding.progressBar.visibility = View.GONE }
                .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                    binding.edTvUsername.setText(documentSnapshot.getString("name"))
                    if (documentSnapshot.getString("email") == null)
                        binding.edTvEmail.setText(getString(R.string.not_provided))
                    else
                        binding.edTvEmail.setText(documentSnapshot.getString("email"))
                    when (documentSnapshot.getString("class")) {
                        null -> binding.classSelection.setSelection(0)
                        "9th" -> binding.classSelection.setSelection(1)
                        "10th" -> binding.classSelection.setSelection(2)
                        "11th" -> binding.classSelection.setSelection(3)
                        "12th" -> binding.classSelection.setSelection(4)
                    }
                    if (documentSnapshot.getString("school") == null) binding.edTvSchool.setText(getString(R.string.not_provided))
                    else binding.edTvSchool.setText(documentSnapshot.getString("school"))
                    if (documentSnapshot.getString("phone") == null) binding.edTvContact.setText("--")
                    else binding.edTvContact.setText(documentSnapshot.getString("phone"))
        }.addOnFailureListener { e: Exception -> Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show() }
    }

    private fun updateProfile(username: String, userClass: String, userEmail: String, userSchool: String, contact: String) {
        binding.progressBar.visibility = View.VISIBLE
        val data: MutableMap<String, Any> = HashMap()
        data["name"] = username
        data["class"] = userClass
        data["email"] = userEmail
        data["school"] = userSchool
        data["phone"] = contact
        if (FirebaseAuth.getInstance().uid != null) {
            FirebaseFirestore.getInstance().collection("users")
                    .document(FirebaseAuth.getInstance().uid!!).set(data)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }.addOnFailureListener { e: Exception -> Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show() }
                .addOnCompleteListener { binding.progressBar.visibility = View.GONE }
        }
    }
}