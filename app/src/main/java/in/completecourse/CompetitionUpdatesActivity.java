package in.completecourse;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import in.completecourse.adapter.CompetitionUpdatesAdapter;
import in.completecourse.app.AppConfig;
import in.completecourse.model.UpdateItem;

public class CompetitionUpdatesActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    private ArrayList<UpdateItem> updatesList;
    private CompetitionUpdatesAdapter adapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        String classStringFinal = SubjectActivity.classString;

        String[] dataObj = new String[1];
        dataObj[0] = classStringFinal;
        GetUpdates getUpdates = new GetUpdates(CompetitionUpdatesActivity.this);
        getUpdates.execute(dataObj);

    }


    private static class GetUpdates extends AsyncTask<String, String, String> {
        private final WeakReference<CompetitionUpdatesActivity> activityWeakReference;
        UpdateItem item;

        GetUpdates(CompetitionUpdatesActivity context){
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            CompetitionUpdatesActivity activity = activityWeakReference.get();
            activity.pDialog = new ProgressDialog(activity);
            activity.pDialog.setMessage("Please wait...");
            activity.pDialog.setCancelable(false);
            activity.pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //final CompetitionUpdatesActivity activity = activityWeakReference.get();
            final CompetitionUpdatesActivity activity = activityWeakReference.get();
            String urlString = AppConfig.URL_COMPETITION_UPDATES;
            String studentclass = params[0];
            URL url;
            InputStream stream;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);

                String data = URLEncoder.encode("classid", "UTF-8")
                        + "=" + URLEncoder.encode(studentclass, "UTF-8");
                urlConnection.connect();
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(data);
                wr.flush();

                stream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8), 8);

                String resFromServer = reader.readLine();
                String status;
                JSONObject jsonResponse;
                activity.updatesList = new ArrayList<>();
                try {
                    jsonResponse = new JSONObject(resFromServer);
                    //Log.e("chatpterItem", String.valueOf(jsonResponse));
                    status = jsonResponse.getString("status");
                    if (status.equals("true")) {
                        JSONObject jsonObject = new JSONObject(resFromServer);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            item =  new UpdateItem();
                            JSONObject chapterObject = jsonArray.getJSONObject(i);
                            item.setUpdateKaName(chapterObject.getString("comptkanaam"));
                            item.setUpdateKaLink(chapterObject.getString("referencelink"));
                            item.setUpdateKaDesc(chapterObject.getString("details"));
                            item.setSerialNumber((i + 1) + ".");
                            activity.updatesList.add(item);
                        }
                    } else {
                        final String msg = jsonResponse.getString("message");
                        activity.runOnUiThread(() -> Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show());
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
            final CompetitionUpdatesActivity activity = activityWeakReference.get();
            if(activity.pDialog.isShowing()) {
                activity.pDialog.dismiss();
            }
            activity.adapter = new CompetitionUpdatesAdapter(activity, activity.updatesList);
            activity.recyclerView.setAdapter(activity.adapter);
        }
    }
}
