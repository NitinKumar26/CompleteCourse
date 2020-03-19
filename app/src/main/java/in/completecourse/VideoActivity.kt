package `in`.completecourse

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

class VideoActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {
    private var videoID: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_video)

        // YouTube player view
        val youTubeView: YouTubePlayerView = findViewById(R.id.youtube_view)

        // Initializing video player with developer key
        youTubeView.initialize(BuildConfig.API_KEY, this)
        videoID = intent.getStringExtra("videoID")
    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider,
                                         errorReason: YouTubeInitializationResult) {
        if (errorReason.isUserRecoverableError) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show()
        } else {
            val errorMessage = String.format(
                    getString(R.string.error_player), errorReason.toString())
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider,
                                         player: YouTubePlayer, wasRestored: Boolean) {
        if (!wasRestored) {
            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
            player.loadVideo(videoID)

            // Hiding player controls
            //player.setPlayerStyle(PlayerStyle.CHROMELESS);
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            youTubePlayerProvider.initialize(BuildConfig.API_KEY, this)
        }
    }

    private val youTubePlayerProvider: YouTubePlayer.Provider
        private get() = findViewById<View>(R.id.youtube_view) as YouTubePlayerView

    companion object {
        private const val RECOVERY_DIALOG_REQUEST = 1
    }
}