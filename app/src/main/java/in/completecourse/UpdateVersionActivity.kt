package `in`.completecourse

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class UpdateVersionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_version)

        //Use an activity context to get the rewarded video instance
        val mAdView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        val updateNowButton = findViewById<Button>(R.id.update_now_button)
        updateNowButton.setOnClickListener { v: View? ->
            val intent = Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=in.completecourse"))
            startActivity(intent)
        }
    }
}