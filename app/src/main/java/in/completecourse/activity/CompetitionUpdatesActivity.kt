package `in`.completecourse.activity

import `in`.completecourse.R
import `in`.completecourse.adapter.CompetitionUpdatesAdapter
import `in`.completecourse.app.AppConfig
import `in`.completecourse.model.UpdateItem
import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_dialog.*
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class CompetitionUpdatesActivity : AppCompatActivity() {
    private var pDialog:ProgressDialog = ProgressDialog(this)
    private var updatesList:ArrayList<UpdateItem>? = null
    private var adapter: CompetitionUpdatesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)

        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val classStringFinal: String? = intent.getStringExtra("class")

        var obj = Array(1) {classStringFinal}
    }

    private class GetUpdates internal constructor(context: CompetitionUpdatesActivity) : AsyncTask<String?, String?, String?>() {
        private val activityWeakReference: WeakReference<CompetitionUpdatesActivity> = WeakReference(context)
        var item: UpdateItem? = null
        override fun onPreExecute() {
            val activity = activityWeakReference.get()
            activity!!.pDialog = ProgressDialog(activity)
            activity.pDialog.setMessage("Please wait...")
            activity.pDialog.setCancelable(false)
            activity.pDialog.show()
        }

        override fun doInBackground(vararg params: String?): String? {
            //final CompetitionUpdatesActivity activity = activityWeakReference.get();
            val activity = activityWeakReference.get()
            val urlString: String = AppConfig.URL_COMPETITION_UPDATES
            val studentclass = params[0]
            val url: URL
            val stream: InputStream
            var urlConnection: HttpURLConnection? = null
            try {
                url = URL(urlString)
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "POST"
                urlConnection.doOutput = true
                val data = (URLEncoder.encode("classid", "UTF-8")
                        + "=" + URLEncoder.encode(studentclass, "UTF-8"))
                urlConnection.connect()
                val wr = OutputStreamWriter(urlConnection.outputStream)
                wr.write(data)
                wr.flush()
                stream = urlConnection.inputStream
                val reader = BufferedReader(InputStreamReader(stream, StandardCharsets.UTF_8), 8)
                val resFromServer = reader.readLine()
                val status: String
                val jsonResponse: JSONObject
                activity!!.updatesList = java.util.ArrayList()
                try {
                    jsonResponse = JSONObject(resFromServer)
                    //Log.e("chatpterItem", String.valueOf(jsonResponse));
                    status = jsonResponse.getString("status")
                    if (status == "true") {
                        val jsonObject = JSONObject(resFromServer)
                        val jsonArray = jsonObject.getJSONArray("data")
                        for (i in 0 until jsonArray.length()) {
                            item = UpdateItem()
                            val chapterObject = jsonArray.getJSONObject(i)
                            item!!.updateKaName = chapterObject.getString("comptkanaam")
                            item!!.updateKaLink = chapterObject.getString("referencelink")
                            item!!.updateKaDesc = chapterObject.getString("details")
                            item!!.serialNumber = (i + 1).toString() + "."
                            activity.updatesList?.add(item!!)
                        }
                    } else {
                        val msg = jsonResponse.getString("message")
                        activity.runOnUiThread { Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show() }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                return null //reader.readLine();
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                urlConnection?.disconnect()
            }
            try {
                Thread.sleep(2000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
                Log.i("Result", "SLEEP ERROR")
            }
            return null
        }

        override fun onPostExecute(jsonObject: String?) {
            val activity = activityWeakReference.get()
            if (activity!!.pDialog.isShowing) {
                activity.pDialog.dismiss()
            }
            activity.adapter = CompetitionUpdatesAdapter(activity, activity.updatesList)
            activity.recyclerView.adapter = activity.adapter
        }
    }
}