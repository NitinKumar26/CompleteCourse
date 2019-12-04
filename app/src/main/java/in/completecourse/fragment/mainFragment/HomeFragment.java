package in.completecourse.fragment.mainFragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import in.completecourse.R;
import in.completecourse.ScanActivity;
import in.completecourse.SearchActivity;
import in.completecourse.SubjectActivity;
import in.completecourse.adapter.ImageAdapter;
import in.completecourse.adapter.SliderAdapter;
import in.completecourse.app.AppConfig;
import in.completecourse.helper.HelperMethods;
import in.completecourse.helper.HttpHandler;
import in.completecourse.model.CardModel;
import in.completecourse.model.Update;


public class HomeFragment extends Fragment {

    private ViewPager mViewPager;
    private ArrayList<Update> updateList;
    private static String urlQR;
    private SliderAdapter sliderAdapter;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 100 && data != null){
            urlQR = data.getStringExtra("url");
            new GetBookName(HomeFragment.this).execute();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ArrayList<CardModel> cardList = new ArrayList<>();
        cardList.add(new CardModel(R.drawable.manual_search, "Manual Search"));
        cardList.add(new CardModel(R.drawable.scan_qr, "Scan QR Code"));
        mViewPager = view.findViewById(R.id.viewPager);
        TabLayout indicator = view.findViewById(R.id.indicator);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        //use a linear layout manager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext() ,2, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        //specify an adapter
        ImageAdapter recyclerViewAdapter = new ImageAdapter(cardList, getContext());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addOnItemTouchListener(new ImageAdapter.RecyclerTouchListener(getContext(), position -> {
            switch (position){
                case 0:
                    Intent searchActivityIntent = new Intent(getContext(), SearchActivity.class);
                    startActivity(searchActivityIntent);
                    break;
                case 1:
                    Intent qrCodeActivityIntent = new Intent(getContext(), ScanActivity.class);
                    startActivityForResult(qrCodeActivityIntent, 100);
                    break;
            }

        }));

        updateList = new ArrayList<>();
        sliderAdapter = new SliderAdapter(getActivity(), updateList);
        mViewPager.setAdapter(sliderAdapter);
        indicator.setupWithViewPager(mViewPager, true);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);

        if (HelperMethods.isNetworkAvailable(getActivity())){

            JSONTransmitter jsonTransmitter = new JSONTransmitter(HomeFragment.this);
            jsonTransmitter.execute();

        }else{
            Toast.makeText(view.getContext(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    private class SliderTimer extends TimerTask {
        @Override
        public void run() {
            if(getActivity() != null){
                getActivity().runOnUiThread(() -> {
                    if (mViewPager.getCurrentItem() < updateList.size() - 1) {
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                    } else {
                        mViewPager.setCurrentItem(0);
                    }
                });
            }
        }
    }

    private static class GetBookName extends AsyncTask<Void, Void, Void> {
        private final WeakReference<HomeFragment> activityWeakReference;

        GetBookName(HomeFragment context){
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            final HomeFragment activity = activityWeakReference.get();
            HttpHandler sh = new HttpHandler();
            String url = urlQR;
            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    String studentSubject = jsonObject.getString("studentsubject");
                    String studentClass = jsonObject.getString("studentclass");
                    Intent intent = new Intent(activity.getActivity(), SubjectActivity.class);
                    intent.putExtra("classCode", studentClass);
                    intent.putExtra("subjectCode", studentSubject);
                    activity.startActivity(intent);
                }catch (final JSONException e) {
                    if (activity.getActivity() != null) {
                        activity.getActivity().runOnUiThread(() -> Toast.makeText(activity.getContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show());
                    }

                }
            } else {
                if (activity.getActivity() != null) {
                    activity.getActivity().runOnUiThread(() -> Toast.makeText(activity.getContext(), "Couldn't get json from server. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG)
                            .show());
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }

    }

    static class JSONTransmitter extends AsyncTask<String, String, String> {
        private final WeakReference<HomeFragment> activityWeakReference;

        JSONTransmitter(HomeFragment context){
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            HomeFragment activity = activityWeakReference.get();
        }

        @Override
        protected String doInBackground(String... params) {
            final HomeFragment activity = activityWeakReference.get();
            String urlString = AppConfig.URL_UPDATES;
            URL url;
            InputStream stream;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.connect();
                stream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8), 8);

                String resFromServer = reader.readLine();
                String status;
                JSONObject jsonResponse;
                try {
                    jsonResponse = new JSONObject(resFromServer);
                    //Log.e("chatpterItem", String.valueOf(jsonResponse));
                    status = jsonResponse.getString("status");
                    if (status.equals("true")){
                        JSONObject jsonObject = new JSONObject(resFromServer);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        Update update;
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject dataObject = jsonArray.getJSONObject(i);
                            update = new Update(dataObject.getString("name"), dataObject.getString("imageurl"));
                            activity.updateList.add(update);
                            activity.sliderAdapter.setItems(activity.updateList);
                            if (activity.getActivity() != null) {
                                activity.getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        activity.sliderAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }

                    }else{
                        final String msg = jsonResponse.getString("message");
                        if (activity.getActivity() != null) {
                            activity.getActivity().runOnUiThread(() -> Toast.makeText(activity.getContext(), msg, Toast.LENGTH_SHORT).show());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null; //reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.i("Result", "SLEEP ERROR");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonObject) {

        }
    }
}



