package `in`.completecourse

import `in`.completecourse.adapter.SpinAdapterNew
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.util.*

class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val userClassArray = arrayOf("Choose Class", "9", "10", "11", "12")

        val spinAdapter = SpinAdapterNew(this, R.layout.spinner_row, userClassArray)

        classSelection.adapter = spinAdapter

        getUserProfile(FirebaseAuth.getInstance().uid)

        btn_done.setOnClickListener{ done() }
    }

    private fun done() {
        var userClass: String? = null
        var userEmail: String
        var userSchool: String
        var contact: String
        val username: String = edTv_username.text.toString()
        userEmail = edTv_email.text.toString()
        userSchool = edTv_school.text.toString()
        contact = edTv_contact.text.toString()
        if (userEmail == "Not Provided") userEmail = ""
        if (userSchool == "Not Provided") userSchool = ""
        if (contact == "Not Provided") contact = ""

        when (classSelection.selectedItemPosition) {
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
                .addOnCompleteListener { progressBar.visibility = View.GONE }
                .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                    edTv_username.setText(documentSnapshot.getString("name"))
                    if (documentSnapshot.getString("email") == null)
                        edTv_email.setText(getString(R.string.not_provided))
                    else
                        edTv_email.setText(documentSnapshot.getString("email"))
                    when (documentSnapshot.getString("class")) {
                        null -> classSelection.setSelection(0)
                        "9th" -> classSelection.setSelection(1)
                        "10th" -> classSelection.setSelection(2)
                        "11th" -> classSelection.setSelection(3)
                        "12th" -> classSelection.setSelection(4)
                    }
                    if (documentSnapshot.getString("school") == null) edTv_school.setText(getString(R.string.not_provided))
                    else edTv_school.setText(documentSnapshot.getString("school"))
                    if (documentSnapshot.getString("phone") == null) edTv_contact.setText("--")
                    else edTv_contact.setText(documentSnapshot.getString("phone"))
        }.addOnFailureListener { e: Exception -> Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show() }
    }

    private fun updateProfile(username: String, userClass: String, userEmail: String, userSchool: String, contact: String) {
        progressBar.visibility = View.VISIBLE
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
                    }
                    .addOnFailureListener { e: Exception -> Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show() }
                    .addOnCompleteListener { progressBar.visibility = View.GONE }
        }
    }
}