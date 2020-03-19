package `in`.completecourse.adapter

import android.content.Context
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import java.util.*

class ClassChaptersAdapter(context: Context, list: ArrayList<ChapterItem>) : RecyclerView.Adapter<ClassChaptersAdapter.MyViewHolder>() {
    private val chapterItemsList: ArrayList<ChapterItem>
    private val context: Context
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.layout_subject_details_row, viewGroup, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        val chapterItem: ChapterItem = chapterItemsList[i]
        myViewHolder.textView.setText(chapterItem.getChapterKaName())
        myViewHolder.serialText.setText(chapterItem.getChapterSerial())
    }

    override fun getItemCount(): Int {
        return chapterItemsList.size
    }

    internal inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView
        val serialText: TextView

        init {
            textView = itemView.findViewById(R.id.textChapter)
            serialText = itemView.findViewById(R.id.text_serial_number)
        }
    }

    interface ClickListener {
        fun onClick(position: Int)
    }

    class RecyclerTouchListener(context: Context?, private val clickListener: ClickListener?) : OnItemTouchListener {
        private val gestureDetector: GestureDetector
        override fun onInterceptTouchEvent(recyclerView: RecyclerView, motionEvent: MotionEvent): Boolean {
            val child = recyclerView.findChildViewUnder(motionEvent.x, motionEvent.y)
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(motionEvent)) {
                clickListener.onClick(recyclerView.getChildAdapterPosition(child))
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
        chapterItemsList = list
        this.context = context
    }
}