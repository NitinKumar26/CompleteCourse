package in.completecourse.adapter;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.completecourse.R;
import in.completecourse.model.NotificationModel;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private final Context context;
    private final ArrayList<NotificationModel> notificationItemList;

    public NotificationAdapter(Context context, ArrayList<NotificationModel> list){
        this.notificationItemList = list;
        this.context = context;
    }


    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.notification_item, viewGroup, false);
        return new NotificationAdapter.NotificationViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder notificationViewHolder, int i) {
        final NotificationModel notificationModel = notificationItemList.get(i);
        notificationViewHolder.notificationHeadline.setText(notificationModel.getmHeading());
        notificationViewHolder.notificationSubHeading.setText(notificationModel.getmSubHeading());
        notificationViewHolder.serial.setText(notificationModel.getSerial());
    }

    @Override
    public int getItemCount() {
        return notificationItemList.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {
        final TextView notificationHeadline, notificationSubHeading, serial;

        NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationHeadline = itemView.findViewById(R.id.notification_head);
            notificationSubHeading = itemView.findViewById(R.id.notification_subhead);
            serial = itemView.findViewById(R.id.serial_notification);
        }
    }

    public interface ClickListener {
        void onClick(int position);
    }
    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private final GestureDetector gestureDetector;

        private final NotificationAdapter.ClickListener clickListener;
        public RecyclerTouchListener(Context context, final NotificationAdapter.ClickListener clickListener){
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });
        }

        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
            View child = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());
            if (child!=null && clickListener != null && gestureDetector.onTouchEvent(motionEvent)){
                clickListener.onClick(recyclerView.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean b) {

        }
    }
}
