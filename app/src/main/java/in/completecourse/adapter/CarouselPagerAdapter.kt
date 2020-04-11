package `in`.completecourse.adapter

import `in`.completecourse.R
import `in`.completecourse.SubjectActivity
import `in`.completecourse.fragment.ItemFragment
import `in`.completecourse.utils.CarouselLinearLayout
import `in`.completecourse.utils.ListConfig
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import kotlinx.android.synthetic.main.activity_subject.*

class CarouselPagerAdapter(private val context: SubjectActivity,
                           private val fragmentManager: FragmentManager,
                           private val classString:String?,
                           private val subjectString:String? ) : FragmentPagerAdapter(fragmentManager), OnPageChangeListener {
    private var scale = 0f

    override fun getItem(position: Int): Fragment {
        // make the first pager bigger than others
        var position = position
        try {
            scale = if (position == SubjectActivity.FIRST_PAGE) BIG_SCALE else SMALL_SCALE
            position %= if (classString.equals("4", ignoreCase = true) ||
                    classString.equals("1", ignoreCase = true)) {
                ListConfig.subjectHighSchool.size
            } else {
                SubjectActivity.count
            }
        }
        catch (e: Exception) { e.printStackTrace() }
        return ItemFragment.newInstance(context, position, scale, classString, subjectString)
    }

    override fun getCount(): Int {
        var count = 0
        try {
            count = SubjectActivity.count * SubjectActivity.LOOPS
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return count
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        try {
            if (positionOffset in 0f..1f) {
                val cur: CarouselLinearLayout = getRootView(position)
                val next: CarouselLinearLayout = getRootView(position + 1)
                cur.setScaleBoth(BIG_SCALE - DIFF_SCALE * positionOffset)
                next.setScaleBoth(SMALL_SCALE + DIFF_SCALE * positionOffset)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPageSelected(position: Int) {}
    override fun onPageScrollStateChanged(state: Int) {}
    private fun getRootView(position: Int): CarouselLinearLayout {
        return fragmentManager.findFragmentByTag(getFragmentTag(position))
                ?.view!!.findViewById<View>(R.id.root_container) as CarouselLinearLayout
    }

    private fun getFragmentTag(position: Int): String {
        return "android:switcher:" + context.viewpagerr.id + ":" + position
    }

    companion object {
        private const val BIG_SCALE = 1.0f
        private const val SMALL_SCALE = 0.7f
        private const val DIFF_SCALE = BIG_SCALE - SMALL_SCALE
    }

}