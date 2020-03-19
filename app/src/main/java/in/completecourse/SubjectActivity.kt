package `in`.completecourse

import `in`.completecourse.SubjectActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager

class SubjectActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject)
        subjectViewPager = findViewById(R.id.viewpagerr)

        //finding view by id and setting the clickListener
        findViewById<View>(R.id.nextItmLy).setOnClickListener(this)
        findViewById<View>(R.id.previceItmLy).setOnClickListener(this)
        findViewById<View>(R.id.imgBack).setOnClickListener(this)
        findViewById<View>(R.id.ic_competition_updates).setOnClickListener(this)
        Companion.intent = intent
        Companion.intent.getStringExtra("type")
        subjectString = intent.getStringExtra("subjectCode")
        classString = intent.getStringExtra("classCode")
        subjectViewPager.setPageMargin(-HelperMethods.getPageMargin(this@SubjectActivity))
        val adapter = CarouselPagerAdapter(this, supportFragmentManager)
        subjectViewPager.setAdapter(adapter)
        adapter.notifyDataSetChanged()
        subjectViewPager.addOnPageChangeListener(adapter)
        subjectViewPager.setOffscreenPageLimit(3)
        setViewPagerItem(classString, subjectString)
        HelperMethods.loadFragment(ClassDetailsFragment(), this@SubjectActivity)
    }

    override fun onClick(view: View) {
        val intent: Intent
        when (view.id) {
            R.id.imgBack -> finish()
            R.id.ic_competition_updates -> {
                intent = Intent(this@SubjectActivity, CompetitionUpdatesActivity::class.java)
                startActivity(intent)
            }
            R.id.nextItmLy -> subjectViewPager!!.currentItem = subjectViewPager!!.currentItem + 1
            R.id.previceItmLy -> subjectViewPager!!.currentItem = subjectViewPager!!.currentItem - 1
        }
    }

    private fun setViewPagerItem(classString: String?, subjectString: String?) {
        if (classString.equals("4", ignoreCase = true) && subjectString.equals("1", ignoreCase = true) || classString.equals("1", ignoreCase = true) && subjectString.equals("2", ignoreCase = true) || classString.equals("2", ignoreCase = true) && subjectString.equals("7", ignoreCase = true) || classString.equals("3", ignoreCase = true) && subjectString.equals("8", ignoreCase = true)) {
            subjectViewPager!!.currentItem = 0
        } else if (classString.equals("4", ignoreCase = true) && subjectString.equals("16", ignoreCase = true) || classString.equals("1", ignoreCase = true) && subjectString.equals("13", ignoreCase = true) || classString.equals("2", ignoreCase = true) && subjectString.equals("9", ignoreCase = true) || classString.equals("3", ignoreCase = true) && subjectString.equals("10", ignoreCase = true)) {
            subjectViewPager!!.currentItem = 1
        } else if (classString.equals("2", ignoreCase = true) && subjectString.equals("5", ignoreCase = true) || classString.equals("3", ignoreCase = true) && subjectString.equals("6", ignoreCase = true) || classString.equals("4", ignoreCase = true) && subjectString.equals("4", ignoreCase = true) || classString.equals("1", ignoreCase = true) && subjectString.equals("3", ignoreCase = true)) {
            subjectViewPager!!.currentItem = 2
        } else if (classString.equals("2", ignoreCase = true) && subjectString.equals("11", ignoreCase = true) || classString.equals("3", ignoreCase = true) && subjectString.equals("12", ignoreCase = true) || classString.equals("4", ignoreCase = true) && subjectString.equals("15", ignoreCase = true) || classString.equals("1", ignoreCase = true) && subjectString.equals("14", ignoreCase = true)) {
            subjectViewPager!!.currentItem = 3
        }
    }

    companion object {
        var subjectViewPager: ViewPager? = null
        const val LOOPS = 4
        const val count = 4
        var intent: Intent? = null

        //public static Spinner classSpinner;
        var subjectString: String? = null
        var classString: String? = null

        /**
         * You shouldn't define first page = 0
         * Let's define first page = 'viewpager size' to make endless carousel
         */
        const val FIRST_PAGE = 4
    }
}