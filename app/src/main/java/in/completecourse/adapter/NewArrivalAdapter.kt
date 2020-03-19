package `in`.completecourse.adapter

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
class NewArrivalAdapter(context: Context, booklist: List<BookNewArrival>) : RecyclerView.Adapter<NewArrivalAdapter.MyViewHolder>() {
    private val booklist: List<BookNewArrival>
    private val context: Context

    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView
        val price: TextView
        val code: TextView
        val thumbnail: ImageView
        val rupeeSign: TextView

        init {
            name = view.findViewById(R.id.title)
            price = view.findViewById(R.id.price)
            code = view.findViewById(R.id.code_store)
            thumbnail = view.findViewById(R.id.thumbnail)
            rupeeSign = view.findViewById(R.id.ruppee_sign)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.new_arrival_item_row, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val book: BookNewArrival = booklist[position]
        holder.name.setText(book.getTitle())
        holder.price.setText(book.getRate())
        holder.code.setText(book.getCode())
        Glide.with(context)
                .asBitmap()
                .load(book.getUrl())
                .placeholder(R.drawable.background_gradient)
                .into(holder.thumbnail)
    }

    override fun getItemCount(): Int {
        return booklist.size
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
        this.booklist = booklist
        this.context = context
    }
}