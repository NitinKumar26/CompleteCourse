package `in`.completecourse.fragment.mainFragment

import `in`.completecourse.PDFActivity
import `in`.completecourse.R
import `in`.completecourse.adapter.NotificationAdapter
import `in`.completecourse.app.AppConfig
import `in`.completecourse.helper.HelperMethods
import `in`.completecourse.helper.HttpHandler
import `in`.completecourse.model.NotificationModel
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.fragment_notification.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.util.*

class NotificationFragment : Fragment(), NotificationAdapter.ClickListener {
    private var itemsList: ArrayList<NotificationModel>? = null
    private var mAdapter: NotificationAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_notification, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemsList = ArrayList<NotificationModel>()
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)

        recyclerView_notification.layoutManager = mLayoutManager
        recyclerView_notification.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))
        recyclerView_notification.isNestedScrollingEnabled = false

        if (HelperMethods.isNetworkAvailable(activity)) {
            GetNotifications(this@NotificationFragment).execute()
        } else {
            Toast.makeText(context, "Please check your internet connection.", Toast.LENGTH_SHORT).show()
        }
    }

    private class GetNotifications internal constructor(context: NotificationFragment) : AsyncTask<Void?, Void?, Void?>() {
        var model: NotificationModel? = null
        private val activityWeakReference: WeakReference<NotificationFragment> = WeakReference(context)
        override fun onPreExecute() {
            val activity = activityWeakReference.get()
            activity!!.progress_bar.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg arg0: Void?): Void? {
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
                        model!!.mHeading = c.getString("notifyheading")
                        model!!.mSubHeading = c.getString("notifydetails")
                        model!!.url = c.getString("notifyURL")
                        model!!.serial = (i + 1).toString() + ". "
                        newArrivalFragment!!.itemsList!!.add(model!!)
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
            notificationFragment!!.progress_bar.visibility = View.GONE
            if (notificationFragment.itemsList!!.isEmpty()) {
                notificationFragment.empty_layout.visibility = View.VISIBLE
            }
            notificationFragment.mAdapter = NotificationAdapter(notificationFragment.activity!!, notificationFragment.itemsList!!)
            notificationFragment.recyclerView_notification.adapter = notificationFragment.mAdapter
            notificationFragment.recyclerView_notification.addOnItemTouchListener(NotificationAdapter.RecyclerTouchListener(notificationFragment.context, notificationFragment))
        }
    }

    override fun onClick(position: Int) {
        val url: String? = itemsList!![position].url
        val intent = Intent(activity, PDFActivity::class.java)
        intent.putExtra("url", url)
        startActivity(intent)
    }
}