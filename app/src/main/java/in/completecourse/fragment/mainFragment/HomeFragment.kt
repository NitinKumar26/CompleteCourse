package `in`.completecourse.fragment.mainFragment

import `in`.completecourse.R
import `in`.completecourse.ScanActivity
import `in`.completecourse.SearchActivity
import `in`.completecourse.SubjectActivity
import `in`.completecourse.adapter.ImageAdapter
import `in`.completecourse.adapter.SliderAdapter
import `in`.completecourse.app.AppConfig
import `in`.completecourse.helper.HelperMethods
import `in`.completecourse.model.CardModel
import `in`.completecourse.model.Update
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*

class HomeFragment : Fragment(), ImageAdapter.ClickListener {
    private var updateList: ArrayList<Update>? = null
    private var sliderAdapter: SliderAdapter? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 100 && data != null) {
            urlQR = data.getStringExtra("url")
            if (urlQR != null) {
                val intent = Intent(context, SubjectActivity::class.java)
                when (urlQR) {
                    "http://completecourse.in/api/GetQRValue/4/1" -> {
                        intent.putExtra("classCode", "4")
                        intent.putExtra("subjectCode", "1")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/4/4" -> {
                        intent.putExtra("classCode", "4")
                        intent.putExtra("subjectCode", "4")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/1/2" -> {
                        intent.putExtra("classCode", "1")
                        intent.putExtra("subjectCode", "2")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/1/3" -> {
                        intent.putExtra("classCode", "1")
                        intent.putExtra("subjectCode", "3")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/2/7" -> {
                        intent.putExtra("classCode", "2")
                        intent.putExtra("subjectCode", "7")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/2/9" -> {
                        intent.putExtra("classCode", "2")
                        intent.putExtra("subjectCode", "9")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/2/11" -> {
                        intent.putExtra("classCode", "2")
                        intent.putExtra("subjectCode", "11")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/2/5" -> {
                        intent.putExtra("classCode", "2")
                        intent.putExtra("subjectCode", "5")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/3/8" -> {
                        intent.putExtra("classCode", "3")
                        intent.putExtra("subjectCode", "8")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/3/10" -> {
                        intent.putExtra("classCode", "3")
                        intent.putExtra("subjectCode", "10")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/3/12" -> {
                        intent.putExtra("classCode", "3")
                        intent.putExtra("subjectCode", "12")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/3/6" -> {
                        intent.putExtra("classCode", "3")
                        intent.putExtra("subjectCode", "6")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/4/16" -> {
                        intent.putExtra("classCode", "4")
                        intent.putExtra("subjectCode", "16")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/4/15" -> {
                        intent.putExtra("classCode", "4")
                        intent.putExtra("subjectCode", "15")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/1/13" -> {
                        intent.putExtra("classCode", "1")
                        intent.putExtra("subjectCode", "15")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/1/14" -> {
                        intent.putExtra("classCode", "1")
                        intent.putExtra("subjectCode", "14")
                        startActivity(intent)
                    }
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val cardList: ArrayList<CardModel> = ArrayList<CardModel>()
        cardList.add(CardModel(R.drawable.manual_search, "Manual Search"))
        cardList.add(CardModel(R.drawable.scan_qr, "Scan QR Code"))
        val indicator: TabLayout = view.findViewById(R.id.indicator)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        //use a linear layout manager
        val gridLayoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager

        //specify an adapter
        val recyclerViewAdapter = ImageAdapter(cardList, context!!)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.addOnItemTouchListener(ImageAdapter.RecyclerTouchListener(context, this))

        updateList = ArrayList()
        sliderAdapter = SliderAdapter(activity, updateList!!)
        viewPager.adapter = sliderAdapter
        indicator.setupWithViewPager(viewPager, true)
        val timer = Timer()
        timer.scheduleAtFixedRate(SliderTimer(), 4000, 6000)
        if (HelperMethods.isNetworkAvailable(activity)) {
            val jsonTransmitter = JSONTransmitter(this@HomeFragment)
            jsonTransmitter.execute()
        } else {
            Toast.makeText(view.context, "Please check your internet connection.", Toast.LENGTH_SHORT).show()
        }
    }

    private inner class SliderTimer : TimerTask() {
        override fun run() {
            if (activity != null) {
                activity!!.runOnUiThread {
                    if (viewPager.currentItem < updateList!!.size - 1) {
                        viewPager.currentItem = viewPager.currentItem + 1
                    } else {
                        viewPager.currentItem = 0
                    }
                }
            }
        }
    }

    internal class JSONTransmitter(context: HomeFragment) : AsyncTask<String?, String?, String?>() {
        private val activityWeakReference: WeakReference<HomeFragment> = WeakReference(context)

        override fun onPreExecute() {
            val activity = activityWeakReference.get()
        }

        override fun doInBackground(vararg params: String?): String? {
            val activity = activityWeakReference.get()
            val urlString: String = AppConfig.URL_UPDATES
            val url: URL
            val stream: InputStream
            var urlConnection: HttpURLConnection? = null
            try {
                url = URL(urlString)
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.doOutput = true
                urlConnection.connect()
                stream = urlConnection.inputStream
                val reader = BufferedReader(InputStreamReader(stream, StandardCharsets.UTF_8), 8)
                val resFromServer = reader.readLine()
                val status: String
                val jsonResponse: JSONObject
                try {
                    jsonResponse = JSONObject(resFromServer)
                    //Log.e("chatpterItem", String.valueOf(jsonResponse));
                    status = jsonResponse.getString("status")
                    if (status == "true") {
                        val jsonObject = JSONObject(resFromServer)
                        val jsonArray = jsonObject.getJSONArray("data")
                        var update: Update
                        for (i in 0 until jsonArray.length()) {
                            val dataObject = jsonArray.getJSONObject(i)
                            update = Update(dataObject.getString("name"), dataObject.getString("imageurl"))
                            activity!!.updateList!!.add(update)
                            activity.sliderAdapter!!.setItems(activity.updateList)
                            if (activity.activity != null) {
                                activity.activity!!.runOnUiThread { activity.sliderAdapter!!.notifyDataSetChanged() }
                            }
                        }
                    } else {
                        val msg = jsonResponse.getString("message")
                        if (activity!!.activity != null) {
                            activity.activity!!.runOnUiThread { Toast.makeText(activity.context, msg, Toast.LENGTH_SHORT).show() }
                        }
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

        override fun onPostExecute(jsonObject: String?) {}

    }

    companion object {
        private var urlQR: String? = null
    }

    override fun onClick(position: Int) {
        when (position) {
            0 -> {
                val searchActivityIntent = Intent(context, SearchActivity::class.java)
                startActivity(searchActivityIntent)
            }
            1 -> {
                val qrCodeActivityIntent = Intent(context, ScanActivity::class.java)
                startActivityForResult(qrCodeActivityIntent, 100)
            }
        }
    }

}