package `in`.completecourse

import `in`.completecourse.fragment.mainFragment.*
import `in`.completecourse.helper.HelperMethods
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar_main)
        val actionBar = supportActionBar
        actionBar?.title = null
        toolbar_title.text = getString(R.string.home)

        val homeFragment = HomeFragment()
        val newArrivalFragment = NewArrivalFragment()
        val notificationFragment = NotificationFragment()
        val profileFragment = ProfileFragments()
        val layoutParams = navigation.layoutParams as CoordinatorLayout.LayoutParams

        navigation.itemIconTintList = null
        layoutParams.behavior = HideBottomViewOnScrollBehavior<View?>()
        navigation.selectedItemId = R.id.action_home
        HelperMethods.loadFragment(homeFragment, this)
        navigation.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.action_home -> {
                    HelperMethods.showFragment(homeFragment, this@MainActivity)
                    HelperMethods.hideFragment(newArrivalFragment, this@MainActivity)
                    HelperMethods.hideFragment(notificationFragment, this@MainActivity)
                    HelperMethods.hideFragment(profileFragment, this@MainActivity)
                    toolbar_title.setText(R.string.home)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_new_arrivals -> {
                    HelperMethods.showFragment(newArrivalFragment, this@MainActivity)
                    HelperMethods.hideFragment(homeFragment, this@MainActivity)
                    HelperMethods.hideFragment(notificationFragment, this@MainActivity)
                    HelperMethods.hideFragment(profileFragment, this@MainActivity)
                    toolbar_title.setText(R.string.new_arrivals)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_notifications -> {
                    HelperMethods.showFragment(notificationFragment, this@MainActivity)
                    HelperMethods.hideFragment(newArrivalFragment, this@MainActivity)
                    HelperMethods.hideFragment(homeFragment, this@MainActivity)
                    HelperMethods.hideFragment(profileFragment, this@MainActivity)
                    toolbar_title.setText(R.string.notifications)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_profile -> {
                    HelperMethods.showFragment(profileFragment, this@MainActivity)
                    HelperMethods.hideFragment(newArrivalFragment, this@MainActivity)
                    HelperMethods.hideFragment(homeFragment, this@MainActivity)
                    HelperMethods.hideFragment(notificationFragment, this@MainActivity)
                    toolbar_title.setText(R.string.profile)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }

}