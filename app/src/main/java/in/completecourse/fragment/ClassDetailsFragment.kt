package `in`.completecourse.fragment

//import com.squareup.okhttp.RequestBody
import `in`.completecourse.PDFActivity
import `in`.completecourse.R
import `in`.completecourse.VideoActivity
import `in`.completecourse.adapter.ClassChaptersAdapter
import `in`.completecourse.model.ChapterItem
import `in`.completecourse.utils.APIService
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_class_details.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import java.io.*
import java.util.*
import kotlin.collections.HashMap

class ClassDetailsFragment : Fragment(), ClassChaptersAdapter.ClickListener {
    private var adapter: ClassChaptersAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_class_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        answer_key_view.isSelected = true
        important_concepts_view.isSelected = false
        video_view.isSelected = false

        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        subjectStringFinal = arguments?.getString("studentSubject")
        classStringFinal = arguments?.getString("studentClass")

        if (getChapters(classStringFinal!!, subjectStringFinal!!)) {
            //Log.e("")

            adapter = ClassChaptersAdapter(context!!, mRecyclerViewItems)
            recyclerView.adapter = adapter
            val count = adapter!!.itemCount
            text_total_answer_key.text = count.toString()
            text_total_important_concepts.text = count.toString()
            text_total_video.text = count.toString()
            text_total_other.text = count.toString()
            recyclerView.addOnItemTouchListener(
                ClassChaptersAdapter.RecyclerTouchListener(context, this@ClassDetailsFragment)
            )

            Log.e("chaptersSize", mRecyclerViewItems.size.toString())
        }else Log.e("No dude", "no")

        //val dataObj = arrayOfNulls<String>(2)
        //dataObj[0] = classStringFinal
        //dataObj[1] = subjectStringFinal
        //val jsonTransmitter = JSONTransmitter(this@ClassDetailsFragment)
        //jsonTransmitter.execute(*dataObj)

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
        val mRecyclerViewItems: MutableList<Any> = ArrayList()

        fun getChapters(studentclass:String, studentSubject:String) : Boolean {

            Log.e("if", "yes")
            //progress_bar.visibility = View.VISIBLE
            //Create Retrofit
            val retrofit = Retrofit.Builder().baseUrl("http://completecourse.in/api/").build()

            //Create Service
            val service = retrofit.create(APIService::class.java)

            //Create HashMap with fields
            val params:HashMap<String?, RequestBody?> = HashMap()
            params["studentclass"] = (studentclass).toRequestBody("text/plain".toMediaTypeOrNull())
            params["studentsubject"] = studentSubject.toRequestBody("text/plain".toMediaTypeOrNull())

            //val yourList:Collection<ChapterItem> = arrayListOf()
            var result = false

            CoroutineScope(Dispatchers.IO).launch {
                    //Do the POST request and get the response
                    val response = service.postClassAndSubjectCode(params)
                    if (response.isSuccessful) {
                        result = true
                        val obj = JSONObject(response.body()?.string() ?: "{}")
                        val jsonArray = obj.getJSONArray("data")//JSONArray(obj.getJSONArray("data"))
                        for (i in 0 until jsonArray.length()) {
                            val item = ChapterItem()
                            val chapterObject = jsonArray.getJSONObject(i)
                            item.chapterKaName = chapterObject.getString("ChapterKaName")
                            item.chapterKaFlipURL = chapterObject.getString("ChapterKaFlipURL")
                            item.conceptKaFlipURL = chapterObject.getString("ConceptKaFlipURL")
                            item.chapterKaVideoID = chapterObject.getString("ChapterKaVideo")
                            item.otherImportantQues = chapterObject.getString("otherimgques")
                            item.chapterSerial = (i + 1).toString() + "."
                            mRecyclerViewItems.add(item)
                        }

                        //val listType: Type = object : TypeToken<List<ChapterItem>>() {}.type
                        //yourList =  Gson().fromJson(jsonArray.toString(), listType)
                        //mRecyclerViewItems.addAll(yourList)
                        Log.e("success", "Yo")
                        Log.e("result", result.toString())
                    }else{
                        Log.e("RETROFIT_ERROR", response.code().toString())
                    }

                    withContext(Dispatchers.Main){
                        //mRecyclerViewItems.addAll(yourList)
                        /*
                        progress_bar.visibility = View.GONE
                        adapter = ClassChaptersAdapter(context!!, mRecyclerViewItems)
                        recyclerView.adapter = adapter
                        val count = adapter!!.itemCount
                        text_total_answer_key.text = count.toString()
                        text_total_important_concepts.text = count.toString()
                        text_total_video.text = count.toString()
                        text_total_other.text = count.toString()
                        recyclerView.addOnItemTouchListener(ClassChaptersAdapter.RecyclerTouchListener(context, this@ClassDetailsFragment))

                        checkIfAdsOn()

                         */
                    }
            }
            Log.e("resultyahan", result.toString())
            return result
        }
    }

    override fun onClick(position: Int) {
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
}