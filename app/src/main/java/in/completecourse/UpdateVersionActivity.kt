package `in`.completecourse

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.activity_update_version.*

class UpdateVersionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_version)

        //Use an activity context to get the rewarded video instance
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        update_now_button.setOnClickListener {
            val intent = Intent("android.intent.action.VIEW",
                    Uri.parse("https://play.google.com/store/apps/details?id=in.completecourse"))
            startActivity(intent)
        }
    }
}