package `in`.completecourse.fragment

import `in`.completecourse.PDFActivity
import `in`.completecourse.R
import `in`.completecourse.VideoActivity
import `in`.completecourse.adapter.ClassChaptersAdapter
import `in`.completecourse.app.AppConfig
import `in`.completecourse.helper.HelperMethods
import `in`.completecourse.model.ChapterItem
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
//import com.facebook.ads.AudienceNetworkAds
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.fragment_class_details.*
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*

class ClassDetailsFragment : Fragment(), ClassChaptersAdapter.ClickListener {
    //private var chapterItemArrayList: ArrayList<ChapterItem>? = null
    private var adapter: ClassChaptersAdapter? = null
    private var mInterstitialAd: InterstitialAd? = null
    private var db: FirebaseFirestore? = null
    private var adsense: Boolean? = null
    private var inHouse: Boolean? = null
    private var interstitial: Boolean? = null
    private var banner: Boolean? = null
    private var mBannerUrl: String? = null
    private var mIconUrl: String? = null
    private var mInstallUrl: String? = null
    private var mName: String? = null
    private var mRating: String? = null
    private var adRequest: AdRequest? = null

    //private var mAdapter: NewArrivalAdapter? = null

    //The AdLoader used to load ads
    private var adLoader: AdLoader? = null

    //List of quizItems and native ads that populate the RecyclerView;
    private val mRecyclerViewItems: MutableList<Any> = ArrayList()

    //List of nativeAds that have been successfully loaded.
    private val mNativeAds: MutableList<UnifiedNativeAd> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //ButterKnife.bind(this, view)
        return inflater.inflate(R.layout.fragment_class_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FirebaseFirestore.getInstance()
        adRequest = AdRequest.Builder().build()
        setAds()
        mInterstitialAd = InterstitialAd(activity)
        mInterstitialAd!!.adUnitId = activity!!.resources.getString(R.string.interstitial_ad_id)
        answer_key_view.isSelected = true
        important_concepts_view.isSelected = false
        video_view.isSelected = false

        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        subjectStringFinal = arguments?.getString("studentSubject")
        classStringFinal = arguments?.getString("studentClass")

        val dataObj = arrayOfNulls<String>(2)
        dataObj[0] = classStringFinal
        dataObj[1] = subjectStringFinal
        val jsonTransmitter = JSONTransmitter(this@ClassDetailsFragment)
        jsonTransmitter.execute(*dataObj)

        answer_key_view.setOnClickListener {
            answerKey()
        }

        important_concepts_view.setOnClickListener {
            importantConcepts()
        }

        video_view.setOnClickListener {
            video()
        }

        other_view.setOnClickListener {
            otherView()
        }

        //HelperMethods.initialize(this)

    }

    private fun answerKey() {
        answer_key_view.isSelected = true
        important_concepts_view.isSelected = false
        video_view.isSelected = false
        other_view.isSelected = false
        if (context != null) {
            //NCERT Answer Key View
            answer_key_view.background = ResourcesCompat.getDrawable(resources, R.drawable.video_selected, null)
            image_answer_key.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_answer_key, null))
            text_answer_key.setTextColor(ResourcesCompat.getColor(resources, R.color.colorWhite, null))
            text_total_answer_key.setTextColor(ResourcesCompat.getColor(resources, R.color.colorWhite, null))

            //Important Concepts View
            important_concepts_view.background = ResourcesCompat.getDrawable(resources, R.drawable.not_selected, null)
            text_total_important_concepts.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
            image_important_concepts.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_concept_default, null))
            text_imp_concepts.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))

            //Video View
            video_view.background = ResourcesCompat.getDrawable(resources, R.drawable.not_selected, null)
            image_video.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_video_player_default, null))
            text_video.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
            text_total_video.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))

            //Other Important Questions View
            other_view.background = ResourcesCompat.getDrawable(resources, R.drawable.not_selected, null)
            text_total_other.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
            image_other.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_imp_question_default, null))
            text_other.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
        }

        //Adhyay TextView
        tv_adhyay_ka_naam.visibility = View.VISIBLE
    }
    private fun importantConcepts() {
        important_concepts_view.isSelected = true
        answer_key_view.isSelected = false
        video_view.isSelected = false
        other_view.isSelected = false
        if (context != null) {
            //Important Concepts View
            important_concepts_view.background = ResourcesCompat.getDrawable(resources, R.drawable.video_selected, null)
            image_important_concepts.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_concept, null))
            text_imp_concepts.setTextColor(ResourcesCompat.getColor(resources, R.color.colorWhite, null))
            text_total_important_concepts.setTextColor(ResourcesCompat.getColor(resources, R.color.colorWhite, null))

            //NCERT Answer Key View
            answer_key_view.background = ResourcesCompat.getDrawable(resources, R.drawable.not_selected, null)
            text_total_answer_key.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
            image_answer_key.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_answer_key_default, null))
            text_answer_key.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))

            //Video View
            video_view.background = ResourcesCompat.getDrawable(resources, R.drawable.not_selected, null)
            image_video.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_video_player_default, null))
            text_video.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
            text_total_video.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))

            //Other Important Questions View
            other_view.background = ResourcesCompat.getDrawable(resources, R.drawable.not_selected, null)
            text_total_other.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
            image_other.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_imp_question_default, null))
            text_other.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
            text_answer_key.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
        }
        tv_adhyay_ka_naam.visibility = View.VISIBLE
    }
    private fun video() {
        video_view.isSelected = true
        answer_key_view.isSelected = false
        important_concepts_view.isSelected = false
        other_view.isSelected = false
        if (context != null) {
            //Video View
            video_view.background = ResourcesCompat.getDrawable(resources, R.drawable.video_selected, null)
            image_video.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_video_player, null))
            text_video.setTextColor(ResourcesCompat.getColor(resources, R.color.colorWhite, null))
            text_total_video.setTextColor(ResourcesCompat.getColor(resources, R.color.colorWhite, null))

            //Important Concepts View
            important_concepts_view.background = ResourcesCompat.getDrawable(resources, R.drawable.not_selected, null)
            text_total_important_concepts.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
            image_important_concepts.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_concept_default, null))
            text_imp_concepts.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
            //Answer Key View
            answer_key_view.background = ResourcesCompat.getDrawable(resources, R.drawable.not_selected, null)
            text_total_answer_key.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
            image_answer_key.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_answer_key_default, null))
            text_answer_key.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
            //Other Important Questions View
            other_view.background = ResourcesCompat.getDrawable(resources, R.drawable.not_selected, null)
            text_total_other.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
            image_other.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_imp_question_default, null))
            text_other.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
        }

        //Adhyay Ka Naam TextView
        tv_adhyay_ka_naam.visibility = View.VISIBLE
    }
    private fun otherView() {
        other_view.isSelected = true
        answer_key_view.isSelected = false
        important_concepts_view.isSelected = false
        video_view.isSelected = false
        if (context != null) {
            //Important Question View
            other_view.background = ResourcesCompat.getDrawable(resources, R.drawable.video_selected, null)
            image_other.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_imp_question, null))
            text_other.setTextColor(ResourcesCompat.getColor(resources, R.color.colorWhite, null))
            text_total_other.setTextColor(ResourcesCompat.getColor(resources, R.color.colorWhite, null))

            //Important Concepts View
            important_concepts_view.background = ResourcesCompat.getDrawable(resources, R.drawable.not_selected, null)
            text_total_important_concepts.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
            image_important_concepts.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_concept_default, null))
            text_imp_concepts.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
            //Answer Key View
            answer_key_view.background = ResourcesCompat.getDrawable(resources, R.drawable.not_selected, null)
            text_total_answer_key.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
            image_answer_key.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_answer_key_default, null))
            text_answer_key.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
            video_view.background = ResourcesCompat.getDrawable(resources, R.drawable.not_selected, null)
            image_video.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_video_player_default, null))
            text_video.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
            text_total_video.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
        }

        //Adhyaya Ka Naam TextView
        tv_adhyay_ka_naam.visibility = View.VISIBLE
    }

    private fun setAds() {
        db!!.collection("flags").document("ads_flags").get().addOnSuccessListener { documentSnapshot ->
            adsense = documentSnapshot.getBoolean("adsense")
            inHouse = documentSnapshot.getBoolean("in_house")
            interstitial = documentSnapshot.getBoolean("interstitial")
            banner = documentSnapshot.getBoolean("banner")
            if ((adsense)!!) {
                if ((banner)!!) {
                    adView_banner_class_details!!.visibility = View.VISIBLE
                    linear_in_house.visibility = View.GONE
                    adView_banner_class_details!!.loadAd(adRequest)
                }
                if ((interstitial)!!) {
                    mInterstitialAd!!.loadAd(AdRequest.Builder().build())
                    mInterstitialAd!!.adListener = object : AdListener() {
                        override fun onAdClosed() {
                            super.onAdClosed()
                            //Load the next interstitial ad
                            mInterstitialAd!!.loadAd(AdRequest.Builder().build())
                        }
                    }
                }
            }
            else if ((inHouse)!!) {
                linear_in_house.visibility = View.VISIBLE
                adView_banner_class_details!!.visibility = View.GONE
                db!!.collection("in_house_ads").whereEqualTo("is_live", true).get().addOnSuccessListener { document: QuerySnapshot ->
                    for (doc: QueryDocumentSnapshot in document) {
                        Log.d("document", doc.id + " => " + doc.data)
                        mBannerUrl = doc.getString("banner_url")
                        mIconUrl = doc.getString("icon_url")
                        mInstallUrl = doc.getString("install_url")
                        mName = doc.getString("name")
                        mRating = doc.get("rating").toString()
                    }
                    if (context != null) {
                        Glide.with((context)!!).load(mBannerUrl).into((app_banner)!!)
                        Glide.with((context)!!).load(mIconUrl).into((app_icon)!!)
                    }
                    app_name.text = mName
                    app_rating.text = mRating
                    btn_install.setOnClickListener {
                        val intentRate = Intent("android.intent.action.VIEW",
                                Uri.parse(mInstallUrl))
                        startActivity(intentRate)
                    }
                }.addOnFailureListener { e: Exception -> Log.e("exception", "exception" + e.message) }
            }
            else {
                adView_banner_class_details!!.visibility = View.GONE
                linear_in_house.visibility = View.GONE
            }
        }
    }

    private fun clear() {
        val size = mRecyclerViewItems.size
        if (size > 0) {
            mRecyclerViewItems.subList(0, size).clear()
            adapter!!.notifyItemRangeRemoved(0, size)
        }
    }

    companion object {
        private var subjectStringFinal: String? = null
        private var classStringFinal: String? = null
    }

    override fun onClick(position: Int) {
        if (mInterstitialAd!!.isLoaded) mInterstitialAd!!.show()
        val intent = Intent(activity, PDFActivity::class.java)
        val intentVideo = Intent(activity, VideoActivity::class.java)
        if (adapter!!.getItemViewType(position) == 0) {
            val chapterItem: ChapterItem = mRecyclerViewItems[position] as ChapterItem
            when {
                answer_key_view.isSelected -> {
                    intent.putExtra("url", chapterItem.chapterKaFlipURL)
                    activity?.startActivity(intent)
                }
                important_concepts_view.isSelected -> {
                    intent.putExtra("url", chapterItem.conceptKaFlipURL)
                    activity?.startActivity(intent)
                }
                video_view.isSelected -> {
                    intentVideo.putExtra("videoID", chapterItem.chapterKaVideoID)
                    activity?.startActivity(intentVideo)
                }
                other_view.isSelected -> {
                    intent.putExtra("url", chapterItem.otherImportantQues)
                    activity?.startActivity(intent)
                }
            }
        }
    }

    internal class JSONTransmitter(context: ClassDetailsFragment) : AsyncTask<String?, String?, String?>() {
        private val activityWeakReference: WeakReference<ClassDetailsFragment> = WeakReference(context)

        override fun onPreExecute() {
            val activity = activityWeakReference.get()
            activity!!.progress_bar.visibility = View.VISIBLE
            activity.clear()
        }

        override fun doInBackground(vararg params: String?): String? {
            val activity = activityWeakReference.get()
            val urlString: String = AppConfig.URL_CHAPTERS
            val studentclass = params[0]
            val studentsubject = params[1]
            val url: URL
            val stream: InputStream
            var urlConnection: HttpURLConnection? = null
            try {
                url = URL(urlString)
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "POST"
                urlConnection.doOutput = true
                var data: String = (URLEncoder.encode("studentclass", "UTF-8")
                        + "=" + URLEncoder.encode(studentclass, "UTF-8"))
                data += ("&" + URLEncoder.encode("studentsubject", "UTF-8") + "="
                        + URLEncoder.encode(studentsubject, "UTF-8"))

                urlConnection.connect()
                val wr = OutputStreamWriter(urlConnection.outputStream)
                wr.write(data)
                wr.flush()
                stream = urlConnection.inputStream
                val reader = BufferedReader(InputStreamReader(stream, StandardCharsets.UTF_8), 8)
                val resFromServer = reader.readLine()
                Log.e("response", resFromServer.toString())
                val status: String
                val jsonResponse: JSONObject
                try {
                    jsonResponse = JSONObject(resFromServer)
                    status = jsonResponse.getString("status")
                    if ((status == "true")) {
                        val jsonObject = JSONObject(resFromServer)
                        val jsonArray = jsonObject.getJSONArray("data")
                        for (i in 0 until jsonArray.length()) {
                            val item = ChapterItem()
                            val chapterObject = jsonArray.getJSONObject(i)
                            item.chapterKaName = chapterObject.getString("ChapterKaName")
                            item.chapterKaFlipURL = chapterObject.getString("ChapterKaFlipURL")
                            item.conceptKaFlipURL = chapterObject.getString("ConceptKaFlipURL")
                            item.chapterKaVideoID = chapterObject.getString("ChapterKaVideo")
                            item.otherImportantQues = chapterObject.getString("otherimgques")
                            item.chapterSerial = (i + 1).toString() + "."
                            activity!!.mRecyclerViewItems.add(item)
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

        override fun onPostExecute(jsonObject: String?) {
            val activity = activityWeakReference.get()
            activity!!.progress_bar.visibility = View.GONE
            activity.adapter = ClassChaptersAdapter((activity.activity)!!, (activity.mRecyclerViewItems))
            activity.recyclerView.adapter = activity.adapter
            val count = activity.adapter!!.itemCount
            activity.text_total_answer_key.text = count.toString()
            activity.text_total_important_concepts.text = count.toString()
            activity.text_total_video.text = count.toString()
            activity.text_total_other.text = count.toString()
            activity.recyclerView!!.addOnItemTouchListener(ClassChaptersAdapter.RecyclerTouchListener(activity.context, activity))

            activity.loadNativeAds()
        }

    }

    private fun insertAdsInMenuItems(mNativeAds: MutableList<UnifiedNativeAd>, mRecyclerViewItems: MutableList<Any>) {
        if (mNativeAds.size <= 0) {
            return
        }
        val offset = mRecyclerViewItems.size / mNativeAds.size + 1
        var index = 0
        for (ad in mNativeAds) {
            mRecyclerViewItems.add(index, ad)
            index += offset
            adapter!!.setItems(mRecyclerViewItems)
            adapter!!.notifyDataSetChanged()
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
}