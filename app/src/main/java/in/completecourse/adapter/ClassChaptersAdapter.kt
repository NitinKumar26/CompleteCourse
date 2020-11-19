package `in`.completecourse.adapter

import `in`.completecourse.R
import `in`.completecourse.helper.HelperMethods
import `in`.completecourse.model.BookNewArrival
import `in`.completecourse.model.ChapterItem
import android.content.Context
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.bumptech.glide.Glide
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import java.util.*

class ClassChaptersAdapter(private val context: Context, list: List<Any>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var chapterItemsList: List<Any>? = list
    private var MENU_ITEM_VIEW_TYPE = 0
    private var UNIFIED_NATIVE_AD_VIEW_TYPE = 1

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textChapter)
        val serialText: TextView = itemView.findViewById(R.id.text_serial_number)

    }

    override fun getItemCount(): Int {
        return chapterItemsList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        val recyclerViewItem: Any = chapterItemsList!![position]
        return if (recyclerViewItem is UnifiedNativeAd) {
            UNIFIED_NATIVE_AD_VIEW_TYPE
        } else MENU_ITEM_VIEW_TYPE
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        return when(i) {
            UNIFIED_NATIVE_AD_VIEW_TYPE -> {
                val unifiedNativeLayoutView: View = LayoutInflater.from(context).inflate(R .layout.unified_class_details, viewGroup, false)
                UnifiedNativeAdViewHolder(unifiedNativeLayoutView)
            }
            MENU_ITEM_VIEW_TYPE -> {
                val view: View = LayoutInflater.from(context).inflate(R.layout.layout_subject_details_row, viewGroup, false)
                MyViewHolder(view)
            }
            else -> {
                val view: View = LayoutInflater.from(context).inflate(R.layout.layout_subject_details_row, viewGroup, false)
                NewArrivalAdapter.MyViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(myViewHolder: RecyclerView.ViewHolder, i: Int) {
        when(getItemViewType(i)){
            UNIFIED_NATIVE_AD_VIEW_TYPE -> {
                val nativeAd = chapterItemsList!![i] as UnifiedNativeAd
                populateAdView(nativeAd, (myViewHolder as UnifiedNativeAdViewHolder).adView)
            }
            MENU_ITEM_VIEW_TYPE -> {
            val holder: MyViewHolder = myViewHolder as MyViewHolder
            val chapterItem: ChapterItem = chapterItemsList?.get(i) as ChapterItem
            holder.textView.text = chapterItem.chapterKaName
            holder.serialText.text = chapterItem.chapterSerial
        }else -> {
            val holder: MyViewHolder = myViewHolder as MyViewHolder
            val chapterItem: ChapterItem = chapterItemsList?.get(i) as ChapterItem
            holder.textView.text = chapterItem.chapterKaName
            holder.serialText.text = chapterItem.chapterSerial
            }
        }
    }

    fun setItems(mBookList: List<Any>?) {
        chapterItemsList = mBookList
    }

    class UnifiedNativeAdViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val adView: UnifiedNativeAdView = view.findViewById(R.id.unified_ad)

        init {

            //adView.mediaView = view.findViewById(R.id.ad_media) as MediaView
            adView.headlineView = view.findViewById(R.id.ad_headline)
            //adView.priceView = view.findViewById(R.id.ad_price)
            //adView.starRatingView = view.findViewById(R.id.ad_stars)
        }
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
                clickListener.onClick(recyclerView.getChildAdapterPosition(child))
            }
            return false
        }

        override fun onTouchEvent(recyclerView: RecyclerView, motionEvent: MotionEvent) {}
        override fun onRequestDisallowInterceptTouchEvent(b: Boolean) {}

    }

    private fun populateAdView(nativeAd: UnifiedNativeAd, adView: UnifiedNativeAdView) {
        //Some assets are guaranteed to be in every UnifiedNativeAd
        (adView.headlineView as TextView).text = nativeAd.headline
        //((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        //(adView.callToActionView as Button).text = nativeAd.callToAction

        //These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        //check before trying to display them
        //val icon = nativeAd.icon
        /*
        if (icon == null) adView.iconView.visibility = View.INVISIBLE else {
            (adView.iconView as ImageView).setImageDrawable(icon.drawable)
            adView.iconView.visibility = View.VISIBLE
        }

         */
    /*
        if (nativeAd.price == null) adView.priceView.visibility = View.INVISIBLE else {
            adView.priceView.visibility = View.VISIBLE
            (adView.priceView as TextView).text = nativeAd.price
        }

     */

        /*
        if (nativeAd.getStore() == null)
            adView.getStoreView().setVisibility(View.INVISIBLE);
        else{
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }
         */

        /*

        if (nativeAd.starRating == null) adView.starRatingView.visibility = View.INVISIBLE else {
            adView.starRatingView.visibility = View.VISIBLE
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating.toFloat()
        }

         */

        /*
        if (nativeAd.getAdvertiser() == null)
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        else{
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
        }
         */

        //Assign native ad object to the native view
        adView.setNativeAd(nativeAd)
    }

}