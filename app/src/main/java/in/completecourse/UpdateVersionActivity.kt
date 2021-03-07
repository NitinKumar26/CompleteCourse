package `in`.completecourse

import `in`.completecourse.databinding.ActivityUpdateVersionBinding
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class UpdateVersionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateVersionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateVersionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.updateNowButton.setOnClickListener {
            val intent = Intent("android.intent.action.VIEW",
                    Uri.parse("https://play.google.com/store/apps/details?id=in.completecourse"))
            startActivity(intent)
        }
    }
}