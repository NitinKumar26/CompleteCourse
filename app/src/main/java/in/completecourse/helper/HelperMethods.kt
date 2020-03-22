package `in`.completecourse.helper

import `in`.completecourse.R
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

object HelperMethods {
    /**
     * Making notification bar transparent
     */
    fun changeStatusBarColor(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    fun loadFragment(fragment: Fragment?, activity: AppCompatActivity) {
        // load fragment
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment!!)
        transaction.commit()
    }

    fun showFragment(fragment: Fragment, activity: AppCompatActivity) {
        val transaction = activity.supportFragmentManager.beginTransaction()
        if (fragment.isAdded) {
            transaction.show(fragment)
        } else {
            transaction.add(R.id.frame_container, fragment)
            transaction.show(fragment)
        }
        transaction.commit()
    }

    fun hideFragment(fragment: Fragment, activity: AppCompatActivity) {
        val transaction = activity.supportFragmentManager.beginTransaction()
        if (fragment.isAdded) transaction.hide(fragment)
        transaction.commit()
    }

    fun getPageMargin(activity: Activity): Int {
        val metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        return metrics.widthPixels / 3 * 2
    }

    /**
     * Checks if there is Internet accessible.
     * @return True if there is Internet. False if not.
     */
    fun isNetworkAvailable(activity: Activity?): Boolean {
        var activeNetworkInfo: NetworkInfo? = null
        if (activity != null) {
            val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            activeNetworkInfo = connectivityManager.activeNetworkInfo
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}