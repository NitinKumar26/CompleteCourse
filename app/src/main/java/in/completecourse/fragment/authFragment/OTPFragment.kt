package `in`.completecourse.fragment.authFragment

import `in`.completecourse.MainActivity
import `in`.completecourse.helper.PrefManager
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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
import kotlinx.android.synthetic.main.fragment_otp_verify.*
import java.util.*
import java.util.concurrent.TimeUnit

class OTPFragment : Fragment() {
    private var mAuth: FirebaseAuth? = null
    private var verificationId: String? = null
    private var username: String? = null
    private var db: FirebaseFirestore? = null
    private var number: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(`in`.completecourse.R.layout.fragment_otp_verify, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        val bundle = arguments
        if (bundle != null) {
            number = bundle.getString("phoneNumber")
            username = bundle.getString("edTvUsername")
            sendVerificationCode("+91$number")
        }
    }

    private fun sendVerificationCode(number: String) {
        progress_bar.visibility = View.VISIBLE
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        )
    }

    private val mCallBack: OnVerificationStateChangedCallbacks = object : OnVerificationStateChangedCallbacks() {
        override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
            super.onCodeSent(s, forceResendingToken)
            verificationId = s
        }

        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            val code = phoneAuthCredential.smsCode
            if (code != null) {
                progress_bar.visibility = View.GONE
                otp_view.setText(code)
                verifyCode(code)
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener { progress_bar.visibility = View.GONE }
                .addOnSuccessListener { authResult: AuthResult ->
                    if (context != null) {
                        if (authResult.user != null) {
                            progress_bar.visibility = View.VISIBLE
                            db!!.collection("users").document(authResult.user!!.uid).get()
                                    .addOnCompleteListener { progress_bar.visibility = View.GONE }
                                    .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                                        if (documentSnapshot.exists()) {
                                            //User details are already in the database
                                            if (context != null) {
                                                val prefManager = PrefManager(context!!)
                                                prefManager.isFirstTimeLaunch = false
                                                val intent = Intent(context, MainActivity::class.java)
                                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                startActivity(intent)
                                            }
                                        } else {
                                            progress_bar.visibility = View.VISIBLE
                                            val userDetails: MutableMap<String, String?> = HashMap()
                                            userDetails["name"] = username
                                            userDetails["phone"] = number
                                            if (authResult.user != null) userDetails["userid"] = authResult.user!!.uid
                                            db!!.collection("users").document(authResult.user!!.uid).set(userDetails)
                                                    .addOnCompleteListener { progress_bar.visibility = View.GONE }.addOnSuccessListener {
                                                        if (context != null) {
                                                            val prefManager = PrefManager(context!!)
                                                            prefManager.isFirstTimeLaunch = false
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