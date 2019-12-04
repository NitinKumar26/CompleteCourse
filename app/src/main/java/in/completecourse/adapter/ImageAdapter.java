package in.completecourse.adapter;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.completecourse.R;
import in.completecourse.model.CardModel;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    private final ArrayList<CardModel> eventList;
    private final Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        final ImageView thumbnail;
        final TextView textName;

        MyViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.thumbnail_company);
            textName = view.findViewById(R.id.title);
        }
    }




    public ImageAdapter(ArrayList<CardModel> eventList, Context context) {
        this.eventList = eventList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        CardModel company = eventList.get(position);
        holder.textName.setText(company.getName());
        // loading album cover using Glide library
        Glide.with(context).load(company.getThumbnail()).into(holder.thumbnail);
        //Picasso.get().load(company.getCompanyPhoto()).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }


    public interface ClickListener {
        void onClick(int position);
    }
    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private final GestureDetector gestureDetector;

        private final ImageAdapter.ClickListener clickListener;
        public RecyclerTouchListener(Context context, final ClickListener clickListener){
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

