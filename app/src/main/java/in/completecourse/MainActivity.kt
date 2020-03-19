package `in`.completecourse

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private var titleText: TextView? = null
    private var homeFragment: HomeFragment? = null
    private var newArrivalFragment: NewArrivalFragment? = null
    private var notificationFragment: NotificationFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //HelperMethods.changeStatusBarColor(MainActivity.this);
        val toolbar = findViewById<Toolbar>(R.id.toolbar_main)
        titleText = findViewById(R.id.toolbar_title)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setTitle(null)
        homeFragment = HomeFragment()
        newArrivalFragment = NewArrivalFragment()
        notificationFragment = NotificationFragment()
        val navigation = findViewById<BottomNavigationView>(R.id.navigation)
        navigation.itemIconTintList = null
        val layoutParams = navigation.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.behavior = HideBottomViewOnScrollBehavior<Any?>()
        navigation.selectedItemId = R.id.action_home
        HelperMethods.loadFragment(homeFragment, this)
        navigation.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.action_home -> {
                    HelperMethods.showFragment(homeFragment, this@MainActivity)
                    HelperMethods.hideFragment(newArrivalFragment, this@MainActivity)
                    HelperMethods.hideFragment(notificationFragment, this@MainActivity)
                    titleText.setText(R.string.home)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_new_arrivals -> {
                    HelperMethods.showFragment(newArrivalFragment, this@MainActivity)
                    HelperMethods.hideFragment(homeFragment, this@MainActivity)
                    HelperMethods.hideFragment(notificationFragment, this@MainActivity)
                    titleText.setText(R.string.new_arrivals)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_notifications -> {
                    HelperMethods.showFragment(notificationFragment, this@MainActivity)
                    HelperMethods.hideFragment(homeFragment, this@MainActivity)
                    HelperMethods.hideFragment(newArrivalFragment, this@MainActivity)
                    titleText.setText(R.string.notifications)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }
}