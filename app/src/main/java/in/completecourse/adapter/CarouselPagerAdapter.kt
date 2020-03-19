package `in`.completecourse.adapter

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener

class CarouselPagerAdapter(context: SubjectActivity, private val fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager), OnPageChangeListener {
    private val context: SubjectActivity
    private var scale = 0f
    override fun getItem(position: Int): Fragment {
        // make the first pager bigger than others
        var position = position
        try {
            scale = if (position == SubjectActivity.FIRST_PAGE) BIG_SCALE else SMALL_SCALE
            if (SubjectActivity.classString.equals("4", ignoreCase = true) ||
                    SubjectActivity.classString.equals("1", ignoreCase = true)) {
                position = position % ListConfig.subjectHighSchool.size
            } else {
                position = position % SubjectActivity.count
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ItemFragment.newInstance(context, position, scale)
    }

    override fun getCount(): Int {
        var count = 0
        try {
            count = SubjectActivity.count * SubjectActivity.LOOPS
        } catch (e: Exception) {
            // TODO: handle exception
            e.printStackTrace()
        }
        return count
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        try {
            if (positionOffset >= 0f && positionOffset <= 1f) {
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
                .getView()!!.findViewById<View>(R.id.root_container) as CarouselLinearLayout
    }

    private fun getFragmentTag(position: Int): String {
        return "android:switcher:" + SubjectActivity.subjectViewPager.getId() + ":" + position
    }

    companion object {
        private const val BIG_SCALE = 1.0f
        private const val SMALL_SCALE = 0.7f
        private const val DIFF_SCALE = BIG_SCALE - SMALL_SCALE
    }

    init {
        this.context = context
    }
}