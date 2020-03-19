package `in`.completecourse.adapter

import `in`.completecourse.model.Update
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import java.util.*

class SliderAdapter(private val mContext: Activity, private var mUpdateList: ArrayList<Update>) : PagerAdapter() {
    private var view: View? = null
    override fun getCount(): Int {
        return mUpdateList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val update = mUpdateList[position]
        val layoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (layoutInflater != null) {
            view = layoutInflater.inflate(R.layout.slider_item, container, false)
            val imageView = view!!.findViewById<ImageView>(R.id.slider_imageView)
            mContext.runOnUiThread {
                Glide.with(mContext)
                        .load(update.url)
                        .placeholder(R.drawable.background_gradient)
                        .into(imageView)
                val viewPager = container as ViewPager
                viewPager.addView(view, 0)
            }
        }
        return view!!
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        val view = `object` as View
        viewPager.removeView(view)
    }

    fun setItems(updates: ArrayList<Update>) {
        mUpdateList = updates
    }

}