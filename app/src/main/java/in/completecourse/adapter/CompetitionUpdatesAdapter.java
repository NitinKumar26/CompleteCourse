package in.completecourse.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.completecourse.PDFActivity;
import in.completecourse.R;
import in.completecourse.model.UpdateItem;

public class CompetitionUpdatesAdapter extends RecyclerView.Adapter<CompetitionUpdatesAdapter.UpdatesViewHolder> {
    private final Context context;
    private final ArrayList<UpdateItem> updateItemsArrayList;

    public CompetitionUpdatesAdapter(Context context, ArrayList<UpdateItem> list){
        this.context = context;
        this.updateItemsArrayList = list;
    }

    @NonNull
    @Override
    public UpdatesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.competition_update_item, viewGroup, false);
        return new UpdatesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UpdatesViewHolder updatesViewHolder, int i) {
        final UpdateItem updateItem = updateItemsArrayList.get(i);
        updatesViewHolder.titleText.setText(updateItem.getUpdateKaName());
        updatesViewHolder.descText.setText(updateItem.getUpdateKaDesc());
        updatesViewHolder.serialText.setText(updateItem.getSerialNumber());

        //updatesViewHolder.titleText.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/Hindi.ttf"));

        updatesViewHolder.titleText.setOnClickListener(v -> {
            if (updatesViewHolder.descText.getVisibility() == View.GONE){
                updatesViewHolder.descText.setVisibility(View.VISIBLE);
                updatesViewHolder.knowMoreText.setVisibility(View.VISIBLE);
            }else{
                updatesViewHolder.descText.setVisibility(View.GONE);
                updatesViewHolder.knowMoreText.setVisibility(View.GONE);
            }
        });

        updatesViewHolder.knowMoreText.setOnClickListener(v -> {
            Intent intent = new Intent(context, PDFActivity.class);
            intent.putExtra("url", updateItem.getUpdateKaLink());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return updateItemsArrayList.size();
    }

    class UpdatesViewHolder extends RecyclerView.ViewHolder {
        final TextView serialText, titleText, descText, knowMoreText;

        UpdatesViewHolder(@NonNull View itemView) {
            super(itemView);
            serialText = itemView.findViewById(R.id.text_serial_number);
            titleText = itemView.findViewById(R.id.textUpdateTitle);
            descText = itemView.findViewById(R.id.desc);
            knowMoreText = itemView.findViewById(R.id.know_more);
        }
    }
}
