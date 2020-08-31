package in.completecourse.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.completecourse.R;
import in.completecourse.adapter.MoreAppsAdapter;
import in.completecourse.helper.HelperMethods;
import in.completecourse.model.MoreAppsItem;

public class MoreAppsActivity extends AppCompatActivity {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private ArrayList<MoreAppsItem>  moreAppsItems;
    private FirebaseFirestore db;
    MoreAppsAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_apps);

        ButterKnife.bind(this);

        db = FirebaseFirestore.getInstance();

        moreAppsItems = new ArrayList<>();

        adapter = new MoreAppsAdapter(this, moreAppsItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));
        recyclerView.setAdapter(adapter);

        getMoreApps();

        recyclerView.addOnItemTouchListener(new HelperMethods.RecyclerTouchListener(this, new HelperMethods.ClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(moreAppsItems.get(position).getApp_play_url()));
                startActivity(intent);
            }
        }));

    }


    private void getMoreApps(){
        db.collection("more_apps_cc").get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
        }).addOnSuccessListener(queryDocumentSnapshots -> {
            List<MoreAppsItem> items = queryDocumentSnapshots.toObjects(MoreAppsItem.class);
            moreAppsItems.addAll(items);
            adapter.setItems(moreAppsItems);
            adapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> Toast.makeText(MoreAppsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());
    }

}
