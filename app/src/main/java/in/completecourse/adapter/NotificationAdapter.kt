package `in`.completecourse.adapter

import android.content.Context
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import java.util.*

class NotificationAdapter(context: Context, list: ArrayList<NotificationModel>) : RecyclerView.Adapter<NotificationViewHolder>() {
    private val context: Context
    private val notificationItemList: ArrayList<NotificationModel>
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): NotificationViewHolder {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.notification_item, viewGroup, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(notificationViewHolder: NotificationViewHolder, i: Int) {
        val notificationModel: NotificationModel = notificationItemList[i]
        notificationViewHolder.notificationHeadline.setText(notificationModel.getmHeading())
        notificationViewHolder.notificationSubHeading.setText(notificationModel.getmSubHeading())
        notificationViewHolder.serial.setText(notificationModel.getSerial())
    }

    override fun getItemCount(): Int {
        return notificationItemList.size
    }

    internal inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notificationHeadline: TextView
        val notificationSubHeading: TextView
        val serial: TextView

        init {
            notificationHeadline = itemView.findViewById(R.id.notification_head)
            notificationSubHeading = itemView.findViewById(R.id.notification_subhead)
            serial = itemView.findViewById(R.id.serial_notification)
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
        notificationItemList = list
        this.context = context
    }
}