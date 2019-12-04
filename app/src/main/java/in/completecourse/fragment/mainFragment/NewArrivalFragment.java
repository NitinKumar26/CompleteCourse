package in.completecourse.fragment.mainFragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import in.completecourse.PDFActivity;
import in.completecourse.R;
import in.completecourse.adapter.NewArrivalAdapter;
import in.completecourse.app.AppConfig;
import in.completecourse.helper.HttpHandler;
import in.completecourse.model.BookNewArrival;


public class NewArrivalFragment extends Fragment {

    private List<BookNewArrival> itemsList;
    private NewArrivalAdapter mAdapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_arrival, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progressbar_fragment_store);
        itemsList = new ArrayList<>();
        mAdapter = new NewArrivalAdapter(view.getContext(), itemsList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(container.getContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        if (isNetworkAvailable()){
            new GetLatestBooks(NewArrivalFragment.this).execute();

        }else{
            Toast.makeText(container.getContext(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }

        return view;
    }
    /**
     * Checks if there is Internet accessible.
     * @return True if there is Internet. False if not.
     */
    private boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = null;
        if (getActivity() != null){
            ConnectivityManager connectivityManager = (ConnectivityManager) ((getActivity())).getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null){
                activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            }
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private static class GetLatestBooks extends AsyncTask<Void, Void, Void> {
        BookNewArrival bookNewArrival;
        private final WeakReference<NewArrivalFragment> activityWeakReference;

        GetLatestBooks(NewArrivalFragment context){
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            NewArrivalFragment newArrivalFragment = activityWeakReference.get();
            HttpHandler sh = new HttpHandler();
            String url = AppConfig.URL_LATEST_BOOKS;
            String jsonStr = sh.makeServiceCall(url);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        bookNewArrival = new BookNewArrival();
                        JSONObject c = jsonArray.getJSONObject(i);
                        bookNewArrival.setTitle(c.getString("arrivalkanaam"));
                        bookNewArrival.setRate(c.getString("arrivalkarate"));
                        bookNewArrival.setUrl(c.getString("arrivalkaimageurl"));
                        bookNewArrival.setSiteUrl(c.getString("arrivalkasiteurl"));
                        newArrivalFragment.itemsList.add(bookNewArrival);
                    }
                } catch (final JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(newArrivalFragment.getActivity(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(newArrivalFragment.getActivity(), "Couldn't get data from server.", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            final NewArrivalFragment newArrivalFragment = activityWeakReference.get();
            newArrivalFragment.mAdapter.notifyDataSetChanged();
            newArrivalFragment.progressBar.setVisibility(View.INVISIBLE);

            newArrivalFragment.recyclerView.addOnItemTouchListener(new NewArrivalAdapter.RecyclerTouchListener(newArrivalFragment.getContext(), position -> {
                String url = newArrivalFragment.itemsList.get(position).getSiteUrl();
                Intent intent = new Intent(newArrivalFragment.getActivity(), PDFActivity.class);
                intent.putExtra("url", url);
                newArrivalFragment.startActivity(intent);
            }));

        }

    }





}