package `in`.completecourse.adapter

import android.content.Context
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.bumptech.glide.Glide
import java.util.*

class ImageAdapter(eventList: ArrayList<CardModel>, context: Context) : RecyclerView.Adapter<ImageAdapter.MyViewHolder>() {
    private val eventList: ArrayList<CardModel>
    private val context: Context

    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thumbnail: ImageView
        val textName: TextView

        init {
            thumbnail = view.findViewById(R.id.thumbnail_company)
            textName = view.findViewById(R.id.title)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.home_card, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val company: CardModel = eventList[position]
        holder.textName.setText(company.getName())
        // loading album cover using Glide library
        Glide.with(context).load(company.getThumbnail()).into(holder.thumbnail)
        //Picasso.get().load(company.getCompanyPhoto()).into(holder.thumbnail);
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    interface ClickListener {
        fun onClick(position: Int)
    }

    class RecyclerTouchListener(context: Context?, private val clickListener: ClickListener?) : OnItemTouchListener {
        private val gestureDetector: GestureDetector
        override fun onInterceptTouchEvent(recyclerView: RecyclerView, motionEvent: MotionEvent): Boolean {
            val child = recyclerView.findChildViewUnder(motionEvent.x, motionEvent.y)
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(motionEvent)) {
                clickListener.onClick(recyclerView.getChildPosition(child))
            }
            return false
        }

        override fun onTouchEvent(recyclerView: RecyclerView, motionEvent: MotionEvent) {}
        override fun onRequestDisallowInterceptTouchEvent(b: Boolean) {}

        init {
            gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }
            })
        }
    }

    init {
        this.eventList = eventList
        this.context = context
    }
}