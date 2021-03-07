package `in`.completecourse

import `in`.completecourse.databinding.ActivityMainBinding
import `in`.completecourse.fragment.mainFragment.*
import `in`.completecourse.helper.HelperMethods
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbarMain)
        val actionBar = supportActionBar
        actionBar?.title = null
        binding.toolbarTitle.text = getString(R.string.home)

        val homeFragment = HomeFragment()
        val newArrivalFragment = NewArrivalFragment()
        val notificationFragment = NotificationFragment()
        val profileFragment = ProfileFragments()
        val layoutParams = binding.navigation.layoutParams as CoordinatorLayout.LayoutParams

        binding.navigation.itemIconTintList = null
        layoutParams.behavior = HideBottomViewOnScrollBehavior<View?>()
        binding.navigation.selectedItemId = R.id.action_home
        HelperMethods.loadFragment(homeFragment, this)
        binding.navigation.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.action_home -> {
                    HelperMethods.showFragment(homeFragment, this@MainActivity)
                    HelperMethods.hideFragment(newArrivalFragment, this@MainActivity)
                    HelperMethods.hideFragment(notificationFragment, this@MainActivity)
                    HelperMethods.hideFragment(profileFragment, this@MainActivity)
                    binding.toolbarTitle.setText(R.string.home)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_new_arrivals -> {
                    HelperMethods.showFragment(newArrivalFragment, this@MainActivity)
                    HelperMethods.hideFragment(homeFragment, this@MainActivity)
                    HelperMethods.hideFragment(notificationFragment, this@MainActivity)
                    HelperMethods.hideFragment(profileFragment, this@MainActivity)
                    binding.toolbarTitle.setText(R.string.new_arrivals)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_notifications -> {
                    HelperMethods.showFragment(notificationFragment, this@MainActivity)
                    HelperMethods.hideFragment(newArrivalFragment, this@MainActivity)
                    HelperMethods.hideFragment(homeFragment, this@MainActivity)
                    HelperMethods.hideFragment(profileFragment, this@MainActivity)
                    binding.toolbarTitle.setText(R.string.notifications)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_profile -> {
                    HelperMethods.showFragment(profileFragment, this@MainActivity)
                    HelperMethods.hideFragment(newArrivalFragment, this@MainActivity)
                    HelperMethods.hideFragment(homeFragment, this@MainActivity)
                    HelperMethods.hideFragment(notificationFragment, this@MainActivity)
                    binding.toolbarTitle.setText(R.string.profile)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }

}