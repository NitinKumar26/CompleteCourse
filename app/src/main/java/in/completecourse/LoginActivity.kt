package `in`.completecourse

import `in`.completecourse.fragment.authFragment.EasyLoginFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loadFragment(EasyLoginFragment())
    }

    /**
     * loading fragment into FrameLayout
     * @param fragment is the fragment which we want to load in our FrameLayout
     */
    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.commit()
    }

}