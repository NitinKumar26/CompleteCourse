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
import in.completecourse.model.ChapterItem;

public class ClassChaptersAdapter extends RecyclerView.Adapter<ClassChaptersAdapter.MyViewHolder> {
    private final ArrayList<ChapterItem> chapterItemsList;
    private final Context context;

    public ClassChaptersAdapter(Context context, ArrayList<ChapterItem> list){
        this.chapterItemsList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_subject_details_row, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        final ChapterItem chapterItem = chapterItemsList.get(i);
        myViewHolder.textView.setText(chapterItem.getChapterKaName());
        myViewHolder.serialText.setText(chapterItem.getChapterSerial());
    }


    @Override
    public int getItemCount() {
        return chapterItemsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView textView, serialText;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textChapter);
            serialText = itemView.findViewById(R.id.text_serial_number);
        }
    }

    public interface ClickListener {
        void onClick(int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private final GestureDetector gestureDetector;

        private final ClassChaptersAdapter.ClickListener clickListener;
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
                clickListener.onClick(recyclerView.getChildAdapterPosition(child));
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
