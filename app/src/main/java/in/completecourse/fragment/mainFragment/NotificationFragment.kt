package `in`.completecourse.fragment.mainFragment

import `in`.completecourse.adapter.NotificationAdapter
import `in`.completecourse.helper.HttpHandler
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import org.json.JSONException
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.util.*

class NotificationFragment : Fragment() {
    private var itemsList: ArrayList<NotificationModel?>? = null
    private var mAdapter: NotificationAdapter? = null

    @BindView(R.id.progress_bar)
    var progressBar: ProgressBar? = null

    @BindView(R.id.recyclerView_notification)
    var recyclerView: RecyclerView? = null

    @BindView(R.id.empty_layout)
    var emptyView: RelativeLayout? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_notification, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemsList = ArrayList<NotificationModel?>()
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))
        recyclerView!!.isNestedScrollingEnabled = false
        if (HelperMethods.isNetworkAvailable(activity)) {
            GetNotifications(this@NotificationFragment).execute()
        } else {
            Toast.makeText(context, "Please check your internet connection.", Toast.LENGTH_SHORT).show()
        }
    }

    private class GetNotifications internal constructor(context: NotificationFragment) : AsyncTask<Void?, Void?, Void?>() {
        var model: NotificationModel? = null
        private val activityWeakReference: WeakReference<NotificationFragment>
        override fun onPreExecute() {
            val activity = activityWeakReference.get()
            activity!!.progressBar!!.visibility = View.VISIBLE
        }

        protected override fun doInBackground(vararg arg0: Void): Void? {
            val newArrivalFragment = activityWeakReference.get()
            val sh = HttpHandler()
            val url: String = AppConfig.URL_NOTIFICATION
            val jsonStr = sh.makeServiceCall(url)
            if (jsonStr != null) {
                try {
                    val jsonObject = JSONObject(jsonStr)
                    val jsonArray = jsonObject.getJSONArray("data")
                    for (i in 0 until jsonArray.length()) {
                        model = NotificationModel()
                        val c = jsonArray.getJSONObject(i)
                        model.setmHeading(c.getString("notifyheading"))
                        model.setmSubHeading(c.getString("notifydetails"))
                        model.setUrl(c.getString("notifyURL"))
                        model.setSerial((i + 1).toString() + ". ")
                        newArrivalFragment!!.itemsList!!.add(model)
                    }
                } catch (e: JSONException) {
                    Toast.makeText(newArrivalFragment!!.activity, "Json parsing error: " + e.message, Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(newArrivalFragment!!.activity, "Couldn't get data from server.", Toast.LENGTH_SHORT).show()
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            val notificationFragment = activityWeakReference.get()
            notificationFragment!!.progressBar!!.visibility = View.GONE
            if (notificationFragment.itemsList!!.isEmpty()) {
                notificationFragment.emptyView!!.visibility = View.VISIBLE
            }
            notificationFragment.mAdapter = NotificationAdapter(notificationFragment.activity!!, notificationFragment.itemsList!!)
            notificationFragment.recyclerView!!.adapter = notificationFragment.mAdapter
            notificationFragment.recyclerView!!.addOnItemTouchListener(NotificationAdapter.RecyclerTouchListener(notificationFragment.context, NotificationAdapter.ClickListener { position: Int ->
                val url: String = notificationFragment.itemsList!![position].getUrl()
                val intent = Intent(notificationFragment.activity, PDFActivity::class.java)
                intent.putExtra("url", url)
                notificationFragment.startActivity(intent)
            }))
        }

        init {
            activityWeakReference = WeakReference(context)
        }
    }
}