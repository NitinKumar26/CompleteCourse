package `in`.completecourse.fragment.mainFragment

import `in`.completecourse.adapter.NewArrivalAdapter
import `in`.completecourse.helper.HttpHandler
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONException
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.util.*

class NewArrivalFragment : Fragment() {
    private var itemsList: MutableList<BookNewArrival?>? = null
    private var mAdapter: NewArrivalAdapter? = null
    private var progressBar: ProgressBar? = null
    private var recyclerView: RecyclerView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_new_arrival, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        progressBar = view.findViewById(R.id.progressbar_fragment_store)
        itemsList = ArrayList<BookNewArrival?>()
        mAdapter = NewArrivalAdapter(view.context, itemsList!!)
        val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(container!!.context, 3)
        recyclerView.setLayoutManager(mLayoutManager)
        recyclerView.setItemAnimator(DefaultItemAnimator())
        recyclerView.setAdapter(mAdapter)
        recyclerView.setNestedScrollingEnabled(false)
        if (isNetworkAvailable) {
            GetLatestBooks(this@NewArrivalFragment).execute()
        } else {
            Toast.makeText(container.context, "Please check your internet connection.", Toast.LENGTH_SHORT).show()
        }
        return view
    }

    /**
     * Checks if there is Internet accessible.
     * @return True if there is Internet. False if not.
     */
    private val isNetworkAvailable: Boolean
        private get() {
            var activeNetworkInfo: NetworkInfo? = null
            if (activity != null) {
                val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (connectivityManager != null) {
                    activeNetworkInfo = connectivityManager.activeNetworkInfo
                }
            }
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    private class GetLatestBooks internal constructor(context: NewArrivalFragment) : AsyncTask<Void?, Void?, Void?>() {
        var bookNewArrival: BookNewArrival? = null
        private val activityWeakReference: WeakReference<NewArrivalFragment>
        protected override fun doInBackground(vararg arg0: Void): Void? {
            val newArrivalFragment = activityWeakReference.get()
            val sh = HttpHandler()
            val url: String = AppConfig.URL_LATEST_BOOKS
            val jsonStr = sh.makeServiceCall(url)
            if (jsonStr != null) {
                try {
                    val jsonObject = JSONObject(jsonStr)
                    val jsonArray = jsonObject.getJSONArray("data")
                    for (i in 0 until jsonArray.length()) {
                        bookNewArrival = BookNewArrival()
                        val c = jsonArray.getJSONObject(i)
                        bookNewArrival.setTitle(c.getString("arrivalkanaam"))
                        bookNewArrival.setRate(c.getString("arrivalkarate"))
                        bookNewArrival.setUrl(c.getString("arrivalkaimageurl"))
                        bookNewArrival.setSiteUrl(c.getString("arrivalkasiteurl"))
                        newArrivalFragment!!.itemsList!!.add(bookNewArrival)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    //Toast.makeText(newArrivalFragment.getActivity(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(newArrivalFragment!!.activity, "Couldn't get data from server.", Toast.LENGTH_SHORT).show()
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            val newArrivalFragment = activityWeakReference.get()
            newArrivalFragment!!.mAdapter!!.notifyDataSetChanged()
            newArrivalFragment.progressBar!!.visibility = View.INVISIBLE
            newArrivalFragment.recyclerView!!.addOnItemTouchListener(NewArrivalAdapter.RecyclerTouchListener(newArrivalFragment.context, NewArrivalAdapter.ClickListener { position: Int ->
                val url: String = newArrivalFragment.itemsList!![position].getSiteUrl()
                val intent = Intent(newArrivalFragment.activity, PDFActivity::class.java)
                intent.putExtra("url", url)
                newArrivalFragment.startActivity(intent)
            }))
        }

        init {
            activityWeakReference = WeakReference(context)
        }
    }
}