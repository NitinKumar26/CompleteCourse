package `in`.completecourse.fragment.mainFragment

import `in`.completecourse.PDFActivity
import `in`.completecourse.R
import `in`.completecourse.adapter.NewArrivalAdapter
import `in`.completecourse.app.AppConfig
import `in`.completecourse.helper.HelperMethods
import `in`.completecourse.helper.HttpHandler
import `in`.completecourse.model.BookNewArrival
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_new_arrival.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.ref.WeakReference

class NewArrivalFragment : Fragment(){
    private var mAdapter: NewArrivalAdapter? = null

    //The AdLoader used to load ads
    private var adLoader: AdLoader? = null

    //List of quizItems and native ads that populate the RecyclerView;
    private val mRecyclerViewItems: MutableList<Any> = java.util.ArrayList()

    //List of nativeAds that have been successfully loaded.
    private val mNativeAds: MutableList<UnifiedNativeAd> = java.util.ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_arrival, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = NewArrivalAdapter(view.context, mRecyclerViewItems)

        val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 3)
        recycler_view_store.layoutManager = mLayoutManager
        recycler_view_store.itemAnimator = DefaultItemAnimator()
        recycler_view_store.adapter = mAdapter
        recycler_view_store.isNestedScrollingEnabled = false

        if (isNetworkAvailable) {
            GetLatestBooks(this@NewArrivalFragment).execute()
        } else {
            Toast.makeText(context, "Please check your internet connection.", Toast.LENGTH_SHORT).show()
        }

        //loadNativeAds()
    }

    /**
     * Checks if there is Internet accessible.
     * @return True if there is Internet. False if not.
     */
    private val isNetworkAvailable: Boolean get() {
            var activeNetworkInfo: NetworkInfo? = null
            if (activity != null) {
                val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                activeNetworkInfo = connectivityManager.activeNetworkInfo
            }
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    private class GetLatestBooks(context: NewArrivalFragment) : AsyncTask<Void?, Void?, Void?>() {
        var bookNewArrival: BookNewArrival? = null
        private val activityWeakReference: WeakReference<NewArrivalFragment> = WeakReference(context)

        override fun doInBackground(vararg arg0: Void?): Void? {
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
                        bookNewArrival!!.title = c.getString("arrivalkanaam")
                        bookNewArrival!!.rate = c.getString("arrivalkarate")
                        bookNewArrival!!.url = c.getString("arrivalkaimageurl")
                        bookNewArrival!!.siteUrl = c.getString("arrivalkasiteurl")
                        newArrivalFragment!!.mRecyclerViewItems.add(bookNewArrival!!)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
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
            newArrivalFragment.progressbar_fragment_store.visibility = View.INVISIBLE

            newArrivalFragment.checkIfAdsOn()

            newArrivalFragment.recycler_view_store.addOnItemTouchListener(HelperMethods.RecyclerTouchListener(newArrivalFragment.context, object : HelperMethods.ClickListener {
                override fun onClick(position: Int) {
                    if (newArrivalFragment.mAdapter!!.getItemViewType(position) == 0){
                        val book:BookNewArrival = newArrivalFragment.mRecyclerViewItems[position] as BookNewArrival
                        val url: String? = book.siteUrl
                        val intent = Intent(newArrivalFragment.context, PDFActivity::class.java)
                        intent.putExtra("url", url)
                        newArrivalFragment.startActivity(intent)
                    }
                }
            }))
        }
    }

    private fun insertAdsInMenuItems(mNativeAds: MutableList<UnifiedNativeAd>, mRecyclerViewItems: MutableList<Any>) {
        if (mNativeAds.size <= 0) {
            return
        }
        val offset = 8  //mRecyclerViewItems.size / mNativeAds.size + 1
        var index = 0
        for (ad in mNativeAds) {
            mRecyclerViewItems.add(index, ad)
            index += offset
            mAdapter!!.setItems(mRecyclerViewItems)
            mAdapter!!.notifyDataSetChanged()
        }
    }

    private fun loadNativeAds() {
        if (context != null) {
            val builder = AdLoader.Builder(context, getString(R.string.native_ad))
            adLoader = builder.forUnifiedNativeAd { unifiedNativeAd ->
                // A native ad loaded successfully, check if the ad loader has finished loading
                // and if so, insert the ads into the list.
                mNativeAds.add(unifiedNativeAd)
                if (!adLoader!!.isLoading) {
                    insertAdsInMenuItems(mNativeAds, mRecyclerViewItems)
                    //adapter.notifyDataSetChanged();
                }
            }.withAdListener(
                    object : AdListener() {
                        override fun onAdFailedToLoad(errorCode: Int) {
                            // A native ad failed to load, check if the ad loader has finished loading
                            // and if so, insert the ads into the list.
                            Log.e("MainActivity", "The previous native ad failed to load. Attempting to" + " load another.")
                            if (!adLoader!!.isLoading) {
                                insertAdsInMenuItems(mNativeAds, mRecyclerViewItems)
                                //adapter.notifyDataSetChanged();
                            }
                        }

                        override fun onAdClicked() {
                            //super.onAdClicked();
                            //Ad Clicked
                            //Log.e("adclicked", "yes")
                        }
                    }).build()

            //Number of Native Ads to load
            val NUMBER_OF_ADS: Int = if (mRecyclerViewItems.size <= 9) 3 else {
                mRecyclerViewItems.size / 5 + 1
            }

            // Load the Native ads.
            adLoader!!.loadAds(AdRequest.Builder().build(), NUMBER_OF_ADS)
            Log.e("numberOfAds", NUMBER_OF_ADS.toString())
        }
    }

    private fun checkIfAdsOn() {
        FirebaseFirestore.getInstance().collection("flags").document("cc_ads").get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.getBoolean("adsense") == true) {
                    Log.e("this", "yes")
                    loadNativeAds()
                }
            }
    }

}