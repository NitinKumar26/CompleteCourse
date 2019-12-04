package in.completecourse.adapter;

import android.annotation.SuppressLint;
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

import java.util.List;

import in.completecourse.R;
import in.completecourse.model.BookNewArrival;

/**
 * RecyclerView adapter class to render items
 * This class can go into another separate class, but for simplicity
 */
public class NewArrivalAdapter extends RecyclerView.Adapter<NewArrivalAdapter.MyViewHolder> {

    private final List<BookNewArrival> booklist;
    private final Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView price;
        final TextView code;
        final ImageView thumbnail;
        final TextView rupeeSign;

        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.title);
            price = view.findViewById(R.id.price);
            code = view.findViewById(R.id.code_store);
            thumbnail = view.findViewById(R.id.thumbnail);
            rupeeSign = view.findViewById(R.id.ruppee_sign);
        }
    }

    public NewArrivalAdapter(Context context, List<BookNewArrival> booklist) {
        this.booklist = booklist;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_arrival_item_row, parent, false);
        return new MyViewHolder(itemView);
    }


    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final BookNewArrival book = booklist.get(position);
        holder.name.setText(book.getTitle());
        holder.price.setText(book.getRate());
        holder.code.setText(book.getCode());

            Glide.with((context))
                    .asBitmap()
                    .load(book.getUrl())
                    .placeholder(R.drawable.background_gradient)
                    .into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return booklist.size();
    }


    public interface ClickListener {
        void onClick(int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private final GestureDetector gestureDetector;

        private final NewArrivalAdapter.ClickListener clickListener;
        public RecyclerTouchListener(Context context, final NewArrivalAdapter.ClickListener clickListener){
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