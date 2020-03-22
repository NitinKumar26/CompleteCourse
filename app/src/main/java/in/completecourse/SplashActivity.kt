package `in`.completecourse

import `in`.completecourse.helper.PrefManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds
import com.google.firebase.firestore.FirebaseFirestore

class SplashActivity : AppCompatActivity() {
    private val versionCodeApp:String = BuildConfig.VERSION_CODE.toString()
    private var versionCode:String? = null
    private val db:FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        MobileAds.initialize(this, this@SplashActivity.resources.getString(R.string.admob_app_id))

        if (isNetworkAvailable()){
            getVersionCode()
        }else{
            Toast.makeText(this@SplashActivity, "Please Check your Internet Connection", Toast.LENGTH_LONG).show()
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val activeNetworkInfo: NetworkInfo?
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun getVersionCode(){
        db.collection("flags").document("version_code").get().addOnSuccessListener { documentSnapshot ->
            versionCode = documentSnapshot.get("current_version_code").toString()
            when {
                versionCode.equals(versionCodeApp) -> {
                    val splashTimeOut = 1500
                    /*
                    Showing splash screen with a timer. This will be useful when you
                    want to showcase your app logo/company
                     */
                    /*
                     * Showing splash screen with a timer. This will be useful when you
                     * want to show case your app logo / company
                     */
                    Handler().postDelayed({

                        // This method will be executed once the timer is over
                        // Start your app main activity
                        // Session manager
                        val session = PrefManager(applicationContext)

                        // Check if user is already logged in or not
                        if (session.isFirstTimeLaunch) {
                            //First time user (Start WelcomeActivity)
                            val intent = Intent(this@SplashActivity, WelcomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            //User is already logged (Start MainActivity)
                            val i = Intent(this@SplashActivity, MainActivity::class.java)
                            startActivity(i)
                            finish()
                        }
                    }, splashTimeOut.toLong())
                }
                versionCode!!.toInt() < versionCodeApp.toInt() -> {
                    //User is on the pre-release version {Start WelcomeActivity}
                    /*
                    This case happens only when developer increase the version code of the app
                    and try to use the app for testing or when developer forget to increase the version code in the console but the latest
                    version of the app is live in Google Play
                     */
                    val intent = Intent(this, WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else -> {
                    //User is not on the latest version {Start UpdateVersion Activity}
                    val intent = Intent(this, UpdateVersionActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}