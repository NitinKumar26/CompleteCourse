package `in`.completecourse.adapter

import `in`.completecourse.R
import `in`.completecourse.helper.HelperMethods
import `in`.completecourse.model.BookNewArrival
import android.annotation.SuppressLint
import android.content.Context
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.bumptech.glide.Glide
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView

/**
 * RecyclerView adapter class to render items
 * This class can go into another separate class, but for simplicity
 */
class NewArrivalAdapter(private val context: Context, private var booklist: List<Any>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var MENU_ITEM_VIEW_TYPE = 0
    private var UNIFIED_NATIVE_AD_VIEW_TYPE = 1

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.title)
        val price: TextView = view.findViewById(R.id.price)
        val code: TextView = view.findViewById(R.id.code_store)
        val thumbnail: ImageView = view.findViewById(R.id.thumbnail)
    }

    override fun getItemCount(): Int {
        return booklist!!.size
    }

    override fun getItemViewType(position: Int): Int {
        val recyclerViewItem: Any = booklist!![position]
        return if (recyclerViewItem is UnifiedNativeAd) {
            UNIFIED_NATIVE_AD_VIEW_TYPE
        } else MENU_ITEM_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            UNIFIED_NATIVE_AD_VIEW_TYPE -> {
                val unifiedNativeLayoutView: View = LayoutInflater.from(context).inflate(R.layout.ad_unified_row, parent, false)
                UnifiedNativeAdViewHolder(unifiedNativeLayoutView)
            }
            MENU_ITEM_VIEW_TYPE -> {
                val view: View = LayoutInflater.from(context).inflate(R.layout.new_arrival_item_row, parent, false)
                MyViewHolder(view)
            }
            else -> {
                val view: View = LayoutInflater.from(context).inflate(R.layout.new_arrival_item_row, parent, false)
                MyViewHolder(view)
            }
        }
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            UNIFIED_NATIVE_AD_VIEW_TYPE -> {
                val nativeAd = booklist!![position] as UnifiedNativeAd
                HelperMethods.populateAdView(nativeAd, (holder as UnifiedNativeAdViewHolder).adView)

            }MENU_ITEM_VIEW_TYPE -> {
                val viewHolder: MyViewHolder = holder as MyViewHolder
                val book: BookNewArrival = booklist!![position] as BookNewArrival
                viewHolder.name.text = book.title
                viewHolder.price.text = book.rate
                viewHolder.code.text = book.code
                Glide.with(context)
                        .asBitmap()
                        .load(book.url)
                        .placeholder(R.drawable.background_gradient)
                        .into(viewHolder.thumbnail)
            }else -> {
                val viewHolder: MyViewHolder = holder as MyViewHolder
                val book: BookNewArrival = booklist!![position] as BookNewArrival
                viewHolder.name.text = book.title
                viewHolder.price.text = book.rate
                viewHolder.code.text = book.code
                Glide.with(context)
                        .asBitmap()
                        .load(book.url)
                        .placeholder(R.drawable.background_gradient)
                        .into(viewHolder.thumbnail)
            }
        }
    }

    fun setItems(mBookList: List<Any>?) {
        booklist = mBookList
    }

    interface ClickListener {
        fun onClick(position: Int)
    }

    class RecyclerTouchListener(context: Context?, private val clickListener: ClickListener?) : OnItemTouchListener {
        private val gestureDetector: GestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }
        })

        override fun onInterceptTouchEvent(recyclerView: RecyclerView, motionEvent: MotionEvent): Boolean {
            val child = recyclerView.findChildViewUnder(motionEvent.x, motionEvent.y)
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(motionEvent)) {
                clickListener.onClick(recyclerView.getChildPosition(child))
            }
            return false
        }

        override fun onTouchEvent(recyclerView: RecyclerView, motionEvent: MotionEvent) {}
        override fun onRequestDisallowInterceptTouchEvent(b: Boolean) {}

    }

    class UnifiedNativeAdViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val adView: UnifiedNativeAdView = view.findViewById(R.id.unified_ad)

        init {

            adView.mediaView = view.findViewById(R.id.ad_media) as MediaView
            adView.headlineView = view.findViewById(R.id.ad_headline)
            adView.priceView = view.findViewById(R.id.ad_price)
            adView.starRatingView = view.findViewById(R.id.ad_stars)
        }
    }

}