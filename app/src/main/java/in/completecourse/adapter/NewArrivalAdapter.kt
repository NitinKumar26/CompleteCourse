package `in`.completecourse.adapter

import `in`.completecourse.R
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

/**
 * RecyclerView adapter class to render items
 * This class can go into another separate class, but for simplicity
 */
class NewArrivalAdapter(private val context: Context, private val booklist: ArrayList<BookNewArrival>?) : RecyclerView.Adapter<NewArrivalAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.title)
        val price: TextView = view.findViewById(R.id.price)
        val code: TextView = view.findViewById(R.id.code_store)
        val thumbnail: ImageView = view.findViewById(R.id.thumbnail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.new_arrival_item_row, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val book: BookNewArrival = booklist!![position]
        holder.name.text = book.title
        holder.price.text = book.rate
        holder.code.text = book.code
        Glide.with(context)
                .asBitmap()
                .load(book.url)
                .placeholder(R.drawable.background_gradient)
                .into(holder.thumbnail)
    }

    override fun getItemCount(): Int {
        return booklist!!.size
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

}