package in.completecourse.fragment.mainFragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.completecourse.PDFActivity;
import in.completecourse.R;
import in.completecourse.adapter.NotificationAdapter;
import in.completecourse.app.AppConfig;
import in.completecourse.helper.HelperMethods;
import in.completecourse.helper.HttpHandler;
import in.completecourse.model.NotificationModel;


public class NotificationFragment extends Fragment {

    private ArrayList<NotificationModel> itemsList;
    private NotificationAdapter mAdapter;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.recyclerView_notification)
    RecyclerView recyclerView;
    @BindView(R.id.empty_layout)
    RelativeLayout emptyView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemsList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));

        recyclerView.setNestedScrollingEnabled(false);

        if (HelperMethods.isNetworkAvailable(getActivity())){
            new GetNotifications(NotificationFragment.this).execute();

        }else{
            Toast.makeText(getContext(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    private static class GetNotifications extends AsyncTask<Void, Void, Void> {
        NotificationModel model;
        private final WeakReference<NotificationFragment> activityWeakReference;

        GetNotifications(NotificationFragment context){
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            NotificationFragment activity = activityWeakReference.get();
            activity.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            NotificationFragment newArrivalFragment = activityWeakReference.get();
            HttpHandler sh = new HttpHandler();
            String url = AppConfig.URL_NOTIFICATION;
            String jsonStr = sh.makeServiceCall(url);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        model = new NotificationModel();
                        JSONObject c = jsonArray.getJSONObject(i);
                        model.setmHeading(c.getString("notifyheading"));
                        model.setmSubHeading(c.getString("notifydetails"));
                        model.setUrl(c.getString("notifyURL"));
                        model.setSerial((i + 1) + ". ");
                        newArrivalFragment.itemsList.add(model);
                    }
                } catch (final JSONException e) {

                    Toast.makeText(newArrivalFragment.getActivity(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(newArrivalFragment.getActivity(), "Couldn't get data from server.", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            final NotificationFragment notificationFragment = activityWeakReference.get();
            notificationFragment.progressBar.setVisibility(View.GONE);
            if (notificationFragment.itemsList.isEmpty()){
                notificationFragment.emptyView.setVisibility(View.VISIBLE);
            }
            notificationFragment.mAdapter = new NotificationAdapter(notificationFragment.getActivity(), notificationFragment.itemsList);
            notificationFragment.recyclerView.setAdapter(notificationFragment.mAdapter);
            notificationFragment.recyclerView.addOnItemTouchListener(new NotificationAdapter.RecyclerTouchListener(notificationFragment.getContext(), position -> {
                String url = notificationFragment.itemsList.get(position).getUrl();
                Intent intent = new Intent(notificationFragment.getActivity(), PDFActivity.class);
                intent.putExtra("url", url);
                notificationFragment.startActivity(intent);
            }));
        }
    }
}



