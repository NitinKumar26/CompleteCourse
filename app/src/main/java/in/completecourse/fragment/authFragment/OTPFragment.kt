package `in`.completecourse.fragment.authFragment

import `in`.completecourse.MainActivity
import `in`.completecourse.databinding.FragmentOtpVerifyBinding
import `in`.completecourse.helper.PrefManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import java.util.concurrent.TimeUnit

class OTPFragment : Fragment() {
    private var verificationId: String? = null
    private var username: String? = null
    private var number: String? = null

    private var _binding: FragmentOtpVerifyBinding? = null
    //This property is only valid between onCreateView and
    //onDestroyView
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOtpVerifyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bundle = arguments
        if (bundle != null) {
            number = bundle.getString("phoneNumber")
            username = bundle.getString("edTvUsername")
            sendVerificationCode("+91$number")
        }

        binding.otpView.setOtpCompletionListener {
            binding.progressBar.visibility = View.GONE
            verifyCode(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun sendVerificationCode(number: String) {
        binding.progressBar.visibility = View.VISIBLE
        activity?.let {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                    it,
                mCallBack
        )
        }
    }

    private val mCallBack: OnVerificationStateChangedCallbacks = object : OnVerificationStateChangedCallbacks() {
        override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
            super.onCodeSent(s, forceResendingToken)
            verificationId = s
        }

        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            val code = phoneAuthCredential.smsCode
            if (code != null) {
                binding.progressBar.visibility = View.GONE
                binding.otpView.setText(code)
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
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { binding.progressBar.visibility = View.GONE }
                .addOnSuccessListener { authResult: AuthResult ->
                    if (context != null) {
                        if (authResult.user != null) {
                            binding.progressBar.visibility = View.VISIBLE
                            FirebaseFirestore.getInstance().collection("users").document(authResult.user!!.uid).get()
                                    .addOnCompleteListener { binding.progressBar.visibility = View.GONE }
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
                                        } else {
                                            binding.progressBar.visibility = View.VISIBLE
                                            val userDetails: MutableMap<String, String?> = HashMap()
                                            userDetails["name"] = username
                                            userDetails["phone"] = number
                                            if (authResult.user != null) userDetails["userid"] = authResult.user!!.uid
                                            FirebaseFirestore.getInstance().collection("users").document(authResult.user!!.uid).set(userDetails)
                                                    .addOnCompleteListener { binding.progressBar.visibility = View.GONE }.addOnSuccessListener {
                                                        if (context != null) {
                                                            val prefManager = PrefManager(context!!)
                                                            prefManager.setFirstTimeLaunch(false)
                                                            val intent = Intent(context, MainActivity::class.java)
                                                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                            startActivity(intent)
                                                        }
                                                    }.addOnFailureListener { e: Exception -> Toast.makeText(context, e.message, Toast.LENGTH_LONG).show() }
                                        }
                                    }.addOnFailureListener { e: Exception ->
                                        Log.e("exception", e.message.toString())
                                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                                    }
                        }
                    }
                }
    }
}