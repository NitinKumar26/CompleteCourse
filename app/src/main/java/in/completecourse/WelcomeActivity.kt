package `in`.completecourse

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener

class WelcomeActivity : AppCompatActivity() {
    private var viewPager: ViewPager? = null
    private var dotsLayout: LinearLayout? = null
    private var layouts: IntArray
    private var btnSkip: Button? = null
    private var btnNext: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        setContentView(R.layout.activity_welcome)
        viewPager = findViewById(R.id.view_pager)
        dotsLayout = findViewById(R.id.layoutDots)
        btnSkip = findViewById(R.id.btn_skip)
        btnNext = findViewById(R.id.btn_next)


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = intArrayOf(
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3,
                R.layout.welcome_slide4)

        // adding bottom dots
        addBottomDots(0)

        // making notification bar transparent
        HelperMethods.changeStatusBarColor(this@WelcomeActivity)
        val myViewPagerAdapter = MyViewPagerAdapter()
        viewPager.setAdapter(myViewPagerAdapter)
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener)
        btnSkip.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(this@WelcomeActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        })
        btnNext.setOnClickListener(View.OnClickListener { v: View? ->
            // checking for last page
            // if last page home screen will be launched
            val nextItem = item
            if (nextItem < layouts.size) {
                // move to next screen
                viewPager.setCurrentItem(nextItem)
            } else {
                val intent = Intent(this@WelcomeActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }

    private fun addBottomDots(currentPage: Int) {
        val dots = arrayOfNulls<TextView>(layouts.size)
        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)
        dotsLayout!!.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]!!.text = Html.fromHtml("&#8226;")
            dots[i]!!.textSize = 35f
            dots[i]!!.setTextColor(colorsInactive[currentPage])
            dotsLayout!!.addView(dots[i])
        }
        if (dots.size > 0) dots[currentPage]!!.setTextColor(colorsActive[currentPage])
    }

    private val item: Int
        private get() = viewPager!!.currentItem + 1

    private fun launchHomeScreen() {
        val prefManager = PrefManager(applicationContext)
        prefManager.setFirstTimeLaunch(false)
        startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
        finish()
    }

    //  viewpager change listener
    private val viewPagerPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            addBottomDots(position)
            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.size - 1) {
                // last page. make button text to GOT IT
                btnNext!!.text = getString(R.string.start)
                btnSkip!!.visibility = View.GONE
            } else {
                // still pages are left
                btnNext!!.text = getString(R.string.next)
                btnSkip!!.visibility = View.VISIBLE
            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }

    /**
     * View pager adapter
     */
    internal inner class MyViewPagerAdapter : PagerAdapter() {
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = LayoutInflater.from(container.context).inflate(layouts[position], container, false)
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return layouts.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }
}