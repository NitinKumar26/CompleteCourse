package `in`.completecourse.fragment.authFragment

import `in`.completecourse.MainActivity
import `in`.completecourse.R
import `in`.completecourse.helper.HelperMethods
import `in`.completecourse.helper.PrefManager
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_easy_login.*
import java.util.*

class EasyLoginFragment : Fragment() {

    private var mAuth: FirebaseAuth? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var pDialog: ProgressDialog? = null
    private var db: FirebaseFirestore? = null
    private var username: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_easy_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        pDialog = ProgressDialog(context)
        pDialog!!.setMessage("Please wait...")

        send_verification_code_button.setOnClickListener {
            sendVerificationCode()
        }

        sign_with_google.setOnClickListener {
            signInWithGoogle()
        }

    }

    private fun sendVerificationCode() {
        val usernameString = edTv_username_login.text.toString().trim { it <= ' ' }
        val mobileNumberString = edTv_mobile_number_login.text.toString().trim { it <= ' ' }
        if (HelperMethods.isNetworkAvailable(activity)) {
            if (usernameString.isNotEmpty() && mobileNumberString.isNotEmpty()) {
                val bundle = Bundle()
                bundle.putString("edTvUsername", usernameString)
                bundle.putString("phoneNumber", mobileNumberString) //Phone Number String
                val otpFragment = OTPFragment()
                otpFragment.arguments = bundle
                loadFragment(otpFragment)
            } else {
                Toast.makeText(context, "Please fill the required details", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Please check your Internet connection.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInWithGoogle() {
        // [START config_signin]
        // Configure Google Sign In
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        // [END config_signin]
        if (context != null) mGoogleSignInClient = GoogleSignIn.getClient(context!!, gso)
        pDialog!!.show()
        signIn()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                //Google sign in was successful, authenticate with Firebase
                Toast.makeText(context, "Dear Learner! Welcome to Complete Course", Toast.LENGTH_SHORT).show()
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    username = account.displayName
                    //userDocId = account.getId();
                    firebaseAuthWithGoogle(account)
                }
            } catch (e: ApiException) {
                //Google Sign in failed update the UI accordingly
                if (pDialog!!.isShowing) pDialog!!.dismiss()
                Toast.makeText(context, "Sign In Failed", Toast.LENGTH_SHORT).show()
                Log.w("LoginActivity", "Google Sign in Failed", e)
            }
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        if (activity != null) {
            mAuth!!.signInWithCredential(credential)
                    .addOnCompleteListener { pDialog!!.hide() }
                    .addOnSuccessListener { authResult: AuthResult ->
                        if (context != null) {
                            if (authResult.user != null) {
                                pDialog!!.show()
                                db!!.collection("users").document(authResult.user!!.uid).get()
                                        .addOnCompleteListener { pDialog!!.dismiss() }
                                        .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                                            if (documentSnapshot.exists()) {
                                                //User details are already in the database
                                                if (context != null) {
                                                    val prefManager = PrefManager(context!!)
                                                    prefManager.setFirstTimeLaunch(false)
                                                    //prefManager.saveUser(documentSnapshot.getString("name"), documentSnapshot.getString("userid"))
                                                    val intent = Intent(context, MainActivity::class.java)
                                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    startActivity(intent)
                                                }
                                            }
                                            else {
                                                pDialog!!.dismiss()
                                                //User details not available in database save them
                                                val userDetails: MutableMap<String, String?> = HashMap()
                                                userDetails["name"] = username
                                                if (mAuth!!.currentUser != null) {
                                                    userDetails["email"] = mAuth!!.currentUser!!.email
                                                    userDetails["userid"] = authResult.user!!.uid
                                                    db!!.collection("users").document(authResult.user!!.uid).set(userDetails)
                                                            .addOnCompleteListener { task: Task<Void?>? -> pDialog!!.dismiss() }.addOnSuccessListener { aVoid: Void? ->
                                                                if (context != null) {
                                                                    val prefManager = PrefManager(context!!)
                                                                    prefManager.setFirstTimeLaunch(false)
                                                                    //prefManager.saveUser(username, authResult.user!!.uid)
                                                                    val intent = Intent(context, MainActivity::class.java)
                                                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                                    startActivity(intent)
                                                                }
                                                            }.addOnFailureListener { e: Exception -> Toast.makeText(context, e.message, Toast.LENGTH_LONG).show() }
                                                }
                                            }
                                        }.addOnFailureListener { e: Exception -> Toast.makeText(context, e.message, Toast.LENGTH_LONG).show() }
                            }
                        }
                    }
        }
    }

    private fun loadFragment(fragment: Fragment) {
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }

}