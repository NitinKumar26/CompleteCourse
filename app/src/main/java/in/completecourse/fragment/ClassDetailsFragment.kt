package `in`.completecourse.fragment

import `in`.completecourse.adapter.ClassChaptersAdapter
import `in`.completecourse.fragment.ClassDetailsFragment
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*

class ClassDetailsFragment() : Fragment() {
    private var chapterItemArrayList: ArrayList<ChapterItem>? = null
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
    private val pDialog: ProgressDialog? = null

    @BindView(R.id.important_concepts_view)
    var importantConcepts: View? = null

    @BindView(R.id.answer_key_view)
    var answerKey: View? = null

    @BindView(R.id.video_view)
    var video: View? = null

    @BindView(R.id.other_view)
    var impQues: View? = null

    @BindView(R.id.adView_banner_class_details)
    var mAdView: AdView? = null

    @BindView(R.id.linear_in_house)
    var mLinearInHouse: LinearLayout? = null

    @BindView(R.id.app_banner)
    var mInHouseBanner: ImageView? = null

    @BindView(R.id.app_icon)
    var mInHouseAppIcon: ImageView? = null

    @BindView(R.id.app_name)
    var mInhouseAppName: TextView? = null

    @BindView(R.id.app_rating)
    var mInHouseRating: TextView? = null

    @BindView(R.id.btn_install)
    var mInHouseInstallButton: Button? = null

    @BindView(R.id.image_answer_key)
    var imageAnswerKey: ImageView? = null

    @BindView(R.id.image_important_concepts)
    var imageImaportantConcepts: ImageView? = null

    @BindView(R.id.image_video)
    var imageVideo: ImageView? = null

    @BindView(R.id.image_other)
    var imageImportantQues: ImageView? = null

    @BindView(R.id.text_answer_key)
    var textAnswerKey: TextView? = null

    @BindView(R.id.text_imp_concepts)
    var textImportantConcepts: TextView? = null

    @BindView(R.id.tv_adhyay_ka_naam)
    var addhyayeTextView: TextView? = null

    @BindView(R.id.text_total_answer_key)
    var textTotalAnswerKey: TextView? = null

    @BindView(R.id.text_total_important_concepts)
    var textTotalImportantConcepts: TextView? = null

    @BindView(R.id.text_total_video)
    var textTotalVideo: TextView? = null

    @BindView(R.id.text_video)
    var textVideo: TextView? = null

    @BindView(R.id.text_total_other)
    var textTotalImpQues: TextView? = null

    @BindView(R.id.text_other)
    var textImportantQues: TextView? = null

    @BindView(R.id.recyclerView)
    var recyclerView: RecyclerView? = null

    @BindView(R.id.progress_bar)
    var progressBar: ProgressBar? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_class_details, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FirebaseFirestore.getInstance()
        adRequest = AdRequest.Builder().build()
        setAds()
        mInterstitialAd = InterstitialAd(Objects.requireNonNull(activity))
        mInterstitialAd!!.adUnitId = activity!!.resources.getString(R.string.interstitial_ad_id)
        answerKey!!.isSelected = true
        importantConcepts!!.isSelected = false
        video!!.isSelected = false
        chapterItemArrayList = ArrayList<ChapterItem>()
        recyclerView!!.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        if (SubjectActivity.intent != null) {
            subjectStringFinal = SubjectActivity.subjectString
            classStringFinal = SubjectActivity.classString
            val dataObj = arrayOfNulls<String>(2)
            dataObj[0] = classStringFinal
            dataObj[1] = subjectStringFinal
            val jsonTransmitter = JSONTransmitter(this@ClassDetailsFragment)
            jsonTransmitter.execute(*dataObj)
        }
        SubjectActivity.subjectViewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {}
            override fun onPageSelected(i: Int) {
                if (classStringFinal.equals(ListConfig.classCode.get(0), ignoreCase = true)) {
                    subjectStringFinal = ListConfig.subjectCodeNinth.get(i % 4)
                } else if (classStringFinal.equals(ListConfig.classCode.get(1), ignoreCase = true)) {
                    subjectStringFinal = ListConfig.subjectCodeTenth.get(i % 4)
                } else if (classStringFinal.equals(ListConfig.classCode.get(2), ignoreCase = true)) {
                    subjectStringFinal = ListConfig.subjectCodeEleven.get(i % 4)
                } else {
                    subjectStringFinal = ListConfig.subjectCodeTwelve.get(i % 4)
                }
                importantConcepts!!.isSelected = false
                answerKey!!.isSelected = true
                answerKey!!.background = (context)!!.resources.getDrawable(R.drawable.video_selected)
                imageAnswerKey!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.ic_answer_key))
                textAnswerKey!!.setTextColor(context!!.resources.getColor(R.color.colorWhite))
                textTotalAnswerKey!!.setTextColor(context!!.resources.getColor(R.color.colorWhite))
                importantConcepts!!.background = context!!.resources.getDrawable(R.drawable.not_selected)
                textTotalImportantConcepts!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))
                imageImaportantConcepts!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.ic_concept_default))
                textImportantConcepts!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))
                addhyayeTextView!!.visibility = View.VISIBLE
                video!!.background = context!!.resources.getDrawable(R.drawable.not_selected)
                textTotalVideo!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))
                imageVideo!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.ic_video_player_default))
                textVideo!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))

                //Other Important Questions View
                impQues!!.background = context!!.resources.getDrawable(R.drawable.not_selected)
                textTotalImpQues!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))
                imageImportantQues!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.ic_imp_question_default))
                textImportantQues!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))
                val dataObj = arrayOfNulls<String>(2)
                dataObj[0] = classStringFinal
                dataObj[1] = subjectStringFinal
                val jsonTransmitter = JSONTransmitter(this@ClassDetailsFragment)
                jsonTransmitter.execute(*dataObj)
            }

            override fun onPageScrollStateChanged(i: Int) {}
        })
    }

    @OnClick(R.id.answer_key_view)
    fun answerKey() {
        answerKey!!.isSelected = true
        importantConcepts!!.isSelected = false
        video!!.isSelected = false
        impQues!!.isSelected = false
        if (context != null) {
            //NCERT Answer Key View
            answerKey!!.background = context!!.resources.getDrawable(R.drawable.video_selected)
            imageAnswerKey!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.ic_answer_key))
            textAnswerKey!!.setTextColor(context!!.resources.getColor(R.color.colorWhite))
            textTotalAnswerKey!!.setTextColor(context!!.resources.getColor(R.color.colorWhite))

            //Important Concepts View
            importantConcepts!!.background = context!!.resources.getDrawable(R.drawable.not_selected)
            textTotalImportantConcepts!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))
            imageImaportantConcepts!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.ic_concept_default))
            textImportantConcepts!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))

            //Video View
            video!!.background = context!!.resources.getDrawable(R.drawable.not_selected)
            imageVideo!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.ic_video_player_default))
            textVideo!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))
            textTotalVideo!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))

            //Other Important Questions View
            impQues!!.background = context!!.resources.getDrawable(R.drawable.not_selected)
            textTotalImpQues!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))
            imageImportantQues!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.ic_imp_question_default))
            textImportantQues!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))
        }

        //Adhyay TextView
        addhyayeTextView!!.visibility = View.VISIBLE
    }

    @OnClick(R.id.important_concepts_view)
    fun importantConcepts() {
        importantConcepts!!.isSelected = true
        answerKey!!.isSelected = false
        video!!.isSelected = false
        impQues!!.isSelected = false
        if (context != null) {
            //Important Concepts View
            importantConcepts!!.background = context!!.resources.getDrawable(R.drawable.video_selected)
            imageImaportantConcepts!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.ic_concept))
            textImportantConcepts!!.setTextColor(context!!.resources.getColor(R.color.colorWhite))
            textTotalImportantConcepts!!.setTextColor(context!!.resources.getColor(R.color.colorWhite))

            //NCERT Answer Key View
            answerKey!!.background = context!!.resources.getDrawable(R.drawable.not_selected)
            textTotalAnswerKey!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))
            imageAnswerKey!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.ic_answer_key_default))
            textAnswerKey!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))

            //Video View
            video!!.background = context!!.resources.getDrawable(R.drawable.not_selected)
            imageVideo!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.ic_video_player_default))
            textVideo!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))
            textTotalVideo!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))

            //Other Important Questions View
            impQues!!.background = context!!.resources.getDrawable(R.drawable.not_selected)
            textTotalImpQues!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))
            imageImportantQues!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.ic_imp_question_default))
            textImportantQues!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))
            textAnswerKey!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))
        }
        addhyayeTextView!!.visibility = View.VISIBLE
    }

    @OnClick(R.id.video_view)
    fun video() {
        video!!.isSelected = true
        answerKey!!.isSelected = false
        importantConcepts!!.isSelected = false
        impQues!!.isSelected = false
        if (context != null) {
            //Video View
            video!!.background = context!!.resources.getDrawable(R.drawable.video_selected)
            imageVideo!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.ic_video_player))
            textVideo!!.setTextColor(context!!.resources.getColor(R.color.colorWhite))
            textTotalVideo!!.setTextColor(context!!.resources.getColor(R.color.colorWhite))

            //Important Concepts View
            importantConcepts!!.background = context!!.resources.getDrawable(R.drawable.not_selected)
            textTotalImportantConcepts!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))
            imageImaportantConcepts!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.ic_concept_default))
            textImportantConcepts!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))
            //Answer Key View
            answerKey!!.background = context!!.resources.getDrawable(R.drawable.not_selected)
            textTotalAnswerKey!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))
            imageAnswerKey!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.ic_answer_key_default))
            textAnswerKey!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))
            //Other Important Questions View
            impQues!!.background = context!!.resources.getDrawable(R.drawable.not_selected)
            textTotalImpQues!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))
            imageImportantQues!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.ic_imp_question_default))
            textImportantQues!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))
        }

        //Adhyay Ka Naam TextView
        addhyayeTextView!!.visibility = View.VISIBLE
    }

    @OnClick(R.id.other_view)
    fun otherView() {
        impQues!!.isSelected = true
        answerKey!!.isSelected = false
        importantConcepts!!.isSelected = false
        video!!.isSelected = false
        if (context != null) {
            //Important Question View
            impQues!!.background = context!!.resources.getDrawable(R.drawable.video_selected)
            imageImportantQues!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.ic_imp_question))
            textImportantQues!!.setTextColor(context!!.resources.getColor(R.color.colorWhite))
            textTotalImpQues!!.setTextColor(context!!.resources.getColor(R.color.colorWhite))

            //Important Concepts View
            importantConcepts!!.background = context!!.resources.getDrawable(R.drawable.not_selected)
            textTotalImportantConcepts!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))
            imageImaportantConcepts!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.ic_concept_default))
            textImportantConcepts!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))
            //Answer Key View
            answerKey!!.background = context!!.resources.getDrawable(R.drawable.not_selected)
            textTotalAnswerKey!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))
            imageAnswerKey!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.ic_answer_key_default))
            textAnswerKey!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))
            video!!.background = context!!.resources.getDrawable(R.drawable.not_selected)
            imageVideo!!.setImageDrawable(context!!.resources.getDrawable(R.drawable.ic_video_player_default))
            textVideo!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))
            textTotalVideo!!.setTextColor(context!!.resources.getColor(R.color.colorBlack))
        }

        //Adhyaya Ka Naam TextView
        addhyayeTextView!!.visibility = View.VISIBLE
    }

    private fun setAds() {
        db!!.collection("flags").document("ads_flags").get().addOnSuccessListener(OnSuccessListener { documentSnapshot ->
            adsense = documentSnapshot.getBoolean("adsense")
            inHouse = documentSnapshot.getBoolean("in_house")
            interstitial = documentSnapshot.getBoolean("interstitial")
            banner = documentSnapshot.getBoolean("banner")
            if ((adsense)!!) {
                if ((banner)!!) {
                    mAdView!!.visibility = View.VISIBLE
                    mLinearInHouse!!.visibility = View.GONE
                    mAdView!!.loadAd(adRequest)
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
            } else if ((inHouse)!!) {
                mLinearInHouse!!.visibility = View.VISIBLE
                mAdView!!.visibility = View.GONE
                db!!.collection("in_house_ads").whereEqualTo("is_live", true).get().addOnSuccessListener({ document: QuerySnapshot ->
                    for (doc: QueryDocumentSnapshot in document) {
                        Log.d("document", doc.getId() + " => " + doc.getData())
                        mBannerUrl = doc.getString("banner_url")
                        mIconUrl = doc.getString("icon_url")
                        mInstallUrl = doc.getString("install_url")
                        mName = doc.getString("name")
                        mRating = doc.get("rating").toString()
                    }
                    if (getContext() != null) {
                        Glide.with((getContext())!!).load(mBannerUrl).into((mInHouseBanner)!!)
                        Glide.with((getContext())!!).load(mIconUrl).into((mInHouseAppIcon)!!)
                    }
                    mInhouseAppName!!.setText(mName)
                    mInHouseRating!!.setText(mRating)
                    mInHouseInstallButton!!.setOnClickListener(View.OnClickListener { v: View? ->
                        val intentRate: Intent = Intent("android.intent.action.VIEW",
                                Uri.parse(mInstallUrl))
                        startActivity(intentRate)
                    })
                }).addOnFailureListener({ e: Exception -> Log.e("exception", "exception" + e.message) })
            } else {
                mAdView!!.visibility = View.GONE
                mLinearInHouse!!.visibility = View.GONE
            }
        })
    }

    internal class JSONTransmitter(context: ClassDetailsFragment) : AsyncTask<String?, String?, String?>() {
        private val activityWeakReference: WeakReference<ClassDetailsFragment>
        override fun onPreExecute() {
            val activity = activityWeakReference.get()
            activity!!.progressBar!!.visibility = View.VISIBLE
            activity.clear()
        }

        protected override fun doInBackground(vararg params: String): String? {
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
                urlConnection!!.requestMethod = "POST"
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
                val status: String
                val jsonResponse: JSONObject
                try {
                    jsonResponse = JSONObject(resFromServer)
                    //Log.e("chatpterItem", String.valueOf(jsonResponse));
                    status = jsonResponse.getString("status")
                    if ((status == "true")) {
                        val jsonObject = JSONObject(resFromServer)
                        val jsonArray = jsonObject.getJSONArray("data")
                        for (i in 0 until jsonArray.length()) {
                            val item = ChapterItem()
                            val chapterObject = jsonArray.getJSONObject(i)
                            item.setChapterKaName(chapterObject.getString("ChapterKaName"))
                            item.setChapterKaFlipURL(chapterObject.getString("ChapterKaFlipURL"))
                            item.setConceptKaFlipURL(chapterObject.getString("ConceptKaFlipURL"))
                            item.setChapterKaVideoID(chapterObject.getString("ChapterKaVideo"))
                            item.setOtherImportantQues(chapterObject.getString("otherimgques"))
                            item.setChapterSerial((i + 1).toString() + ".")
                            activity!!.chapterItemArrayList!!.add(item)
                        }
                    } else {
                        val msg = jsonResponse.getString("message")
                        if (activity!!.activity != null) {
                            activity.activity!!.runOnUiThread({ Toast.makeText(activity.getContext(), msg, Toast.LENGTH_SHORT).show() })
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
            activity!!.progressBar!!.visibility = View.GONE
            activity.adapter = ClassChaptersAdapter((activity.activity)!!, (activity.chapterItemArrayList)!!)
            activity.recyclerView!!.adapter = activity.adapter
            val count = activity.adapter!!.itemCount
            activity.textTotalAnswerKey!!.text = count.toString()
            activity.textTotalImportantConcepts!!.text = count.toString()
            activity.textTotalVideo!!.text = count.toString()
            activity.textTotalImpQues!!.text = count.toString()
            activity.recyclerView!!.addOnItemTouchListener(ClassChaptersAdapter.RecyclerTouchListener(activity.context, ClassChaptersAdapter.ClickListener { position: Int ->
                if (activity.mInterstitialAd!!.isLoaded()) activity.mInterstitialAd!!.show()
                val intent: Intent = Intent(activity.getActivity(), PDFActivity::class.java)
                val intentVideo: Intent = Intent(activity.getActivity(), VideoActivity::class.java)
                if (activity.answerKey!!.isSelected()) {
                    intent.putExtra("url", activity.chapterItemArrayList!!.get(position).getChapterKaFlipURL())
                    activity.startActivity(intent)
                } else if (activity.importantConcepts!!.isSelected()) {
                    intent.putExtra("url", activity.chapterItemArrayList!!.get(position).getConceptKaFlipURL())
                    activity.startActivity(intent)
                } else if (activity.video!!.isSelected()) {
                    intentVideo.putExtra("videoID", activity.chapterItemArrayList!!.get(position).getChapterKaVideoID())
                    activity.startActivity(intentVideo)
                } else if (activity.impQues!!.isSelected()) {
                    intent.putExtra("url", activity.chapterItemArrayList!!.get(position).getOtherImportantQues())
                    activity.startActivity(intent)
                }
            }))
        }

        init {
            activityWeakReference = WeakReference(context)
        }
    }

    private fun clear() {
        val size = chapterItemArrayList!!.size
        if (size > 0) {
            chapterItemArrayList!!.subList(0, size).clear()
            adapter!!.notifyItemRangeRemoved(0, size)
        }
    }

    companion object {
        private var subjectStringFinal: String? = null
        private var classStringFinal: String? = null
    }
}