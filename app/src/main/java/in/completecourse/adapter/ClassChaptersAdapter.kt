package `in`.completecourse.adapter

import `in`.completecourse.R
import `in`.completecourse.model.ChapterItem
import android.content.Context
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener

class ClassChaptersAdapter(private val context: Context, list: List<Any>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var chapterItemsList: List<Any>? = list

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textChapter)
        val serialText: TextView = itemView.findViewById(R.id.text_serial_number)

    }

    override fun getItemCount(): Int {
        return chapterItemsList!!.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.layout_subject_details_row, viewGroup, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(myViewHolder: RecyclerView.ViewHolder, i: Int) {
        val holder: MyViewHolder = myViewHolder as MyViewHolder
        val chapterItem: ChapterItem = chapterItemsList?.get(i) as ChapterItem
        holder.textView.text = chapterItem.chapterKaName
        holder.serialText.text = chapterItem.chapterSerial
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

}