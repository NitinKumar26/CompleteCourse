package `in`.completecourse

import `in`.completecourse.adapter.CarouselPagerAdapter
import `in`.completecourse.adapter.ClassChaptersAdapter
import `in`.completecourse.databinding.ActivitySubjectBinding
import `in`.completecourse.helper.HelperMethods
import `in`.completecourse.model.ChapterItem
import `in`.completecourse.utils.APIService
import `in`.completecourse.utils.ListConfig
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.MutableList
import kotlin.collections.set


class SubjectActivity : AppCompatActivity(), View.OnClickListener, ClassChaptersAdapter.ClickListener {
    var classString:String? = null
    var subjectString:String? = null
    var spinPosition:String? = null

    private lateinit var binding: ActivitySubjectBinding
    private val mRecyclerViewItems: MutableList<Any> = ArrayList()
    private var adapter: ClassChaptersAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubjectBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.nextItmLy.setOnClickListener(this)
        binding.previceItmLy.setOnClickListener(this)
        binding.imgBack.setOnClickListener(this)
        binding.icCompetitionUpdates.setOnClickListener(this)

        classString = intent.getStringExtra("classCode")
        subjectString = intent.getStringExtra("subjectCode")
        spinPosition = intent.getStringExtra("spinPosition")

        binding.viewpagerr.pageMargin = -HelperMethods.getPageMargin(this@SubjectActivity)
        val adapter = CarouselPagerAdapter(this, supportFragmentManager, classString, subjectString)
        binding.viewpagerr.adapter = adapter
        adapter.notifyDataSetChanged()
        binding.viewpagerr.addOnPageChangeListener(adapter)
        binding.viewpagerr.offscreenPageLimit = 3
        setViewPagerItem(classString, subjectString)

        binding.viewpagerr.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {}

            override fun onPageSelected(i: Int) {
                subjectString = when {
                    classString.equals(ListConfig.classCode[0]) -> { ListConfig.subjectCodeNinth[i % 4] }
                    classString.equals(ListConfig.classCode[1]) -> { ListConfig.subjectCodeTenth[i % 4] }
                    classString.equals(ListConfig.classCode[2]) -> { ListConfig.subjectCodeEleven[i % 4] }
                    else -> { ListConfig.subjectCodeTwelve[i % 4] }
                }

                getChapters(classString!!, subjectString!!)
            }
            override fun onPageScrollStateChanged(i: Int) {}
        })

        binding.answerKeyView.isSelected = true
        binding.importantConceptsView.isSelected = false
        binding.videoView.isSelected = false

        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        binding.answerKeyView.setOnClickListener { answerKey() }

        binding.importantConceptsView.setOnClickListener { importantConcepts() }

        binding.videoView.setOnClickListener { video() }

        binding.otherView.setOnClickListener { otherView() }

        getChapters(classString!!, subjectString!!)

    }

    private fun answerKey() {
        binding.answerKeyView.isSelected = true
        binding.importantConceptsView.isSelected = false
        binding.videoView.isSelected = false
        binding.otherView.isSelected = false

        //NCERT Answer Key View
        binding.answerKeyView.background = ResourcesCompat.getDrawable(resources, R.drawable.video_selected, null)
        binding.imageAnswerKey.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_answer_key, null))
        binding.textAnswerKey.setTextColor(ResourcesCompat.getColor(resources, R.color.colorWhite, null))
        binding.textTotalAnswerKey.setTextColor(ResourcesCompat.getColor(resources, R.color.colorWhite, null))

        //Important Concepts View
        binding.importantConceptsView.background = ResourcesCompat.getDrawable(resources, R.drawable.not_selected, null)
        binding.textTotalImportantConcepts.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
        binding.imageImportantConcepts.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_concept_default, null))
        binding.textImpConcepts.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))

        //Video View
        binding.videoView.background = ResourcesCompat.getDrawable(resources, R.drawable.not_selected, null)
        binding.imageVideo.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_video_player_default, null))
        binding.textVideo.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
        binding.textTotalVideo.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))

        //Other Important Questions View
        binding.otherView.background = ResourcesCompat.getDrawable(resources, R.drawable.not_selected, null)
        binding.textTotalOther.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
        binding.imageOther.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_imp_question_default, null))
        binding.textOther.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))

        //Adhyay TextView
        binding.tvAdhyayKaNaam.visibility = View.VISIBLE
    }

    private fun importantConcepts() {
        binding.importantConceptsView.isSelected = true
        binding.answerKeyView.isSelected = false
        binding.videoView.isSelected = false
        binding.otherView.isSelected = false

        //Important Concepts View
        binding.importantConceptsView.background = ResourcesCompat.getDrawable(resources, R.drawable.video_selected, null)
        binding.imageImportantConcepts.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_concept, null))
        binding.textImpConcepts.setTextColor(ResourcesCompat.getColor(resources, R.color.colorWhite, null))
        binding.textTotalImportantConcepts.setTextColor(ResourcesCompat.getColor(resources, R.color.colorWhite, null))

        //NCERT Answer Key View
        binding.answerKeyView.background = ResourcesCompat.getDrawable(resources, R.drawable.not_selected, null)
        binding.textTotalAnswerKey.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
        binding.imageAnswerKey.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_answer_key_default, null))
        binding.textAnswerKey.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))

        //Video View
        binding.videoView.background = ResourcesCompat.getDrawable(resources, R.drawable.not_selected, null)
        binding.imageVideo.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_video_player_default, null))
        binding.textVideo.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
        binding.textTotalVideo.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))

        //Other Important Questions View
        binding.otherView.background = ResourcesCompat.getDrawable(resources, R.drawable.not_selected, null)
        binding.textTotalOther.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
        binding.imageOther.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_imp_question_default, null))
        binding.textOther.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
        binding.textAnswerKey.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))

        binding.tvAdhyayKaNaam.visibility = View.VISIBLE
    }

    private fun video() {
        binding.videoView.isSelected = true
        binding.answerKeyView.isSelected = false
        binding.importantConceptsView.isSelected = false
        binding.otherView.isSelected = false
        //Video View
        binding.videoView.background = ResourcesCompat.getDrawable(resources, R.drawable.video_selected, null)
        binding.imageVideo.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_video_player, null))
        binding.textVideo.setTextColor(ResourcesCompat.getColor(resources, R.color.colorWhite, null))
        binding.textTotalVideo.setTextColor(ResourcesCompat.getColor(resources, R.color.colorWhite, null))

        //Important Concepts View
        binding.importantConceptsView.background = ResourcesCompat.getDrawable(resources, R.drawable.not_selected, null)
        binding.textTotalImportantConcepts.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
        binding.imageImportantConcepts.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_concept_default, null))
        binding.textImpConcepts.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
        //Answer Key View
        binding.answerKeyView.background = ResourcesCompat.getDrawable(resources, R.drawable.not_selected, null)
        binding.textTotalAnswerKey.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
        binding.imageAnswerKey.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_answer_key_default, null))
        binding.textAnswerKey.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
        //Other Important Questions View
        binding.otherView.background = ResourcesCompat.getDrawable(resources, R.drawable.not_selected, null)
        binding.textTotalOther.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
        binding.imageOther.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_imp_question_default, null))
        binding.textOther.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))

        //Adhyay Ka Naam TextView
        binding.tvAdhyayKaNaam.visibility = View.VISIBLE
    }

    private fun otherView() {
        binding.otherView.isSelected = true
        binding.answerKeyView.isSelected = false
        binding.importantConceptsView.isSelected = false
        binding.videoView.isSelected = false

        //Important Question View
        binding.otherView.background = ResourcesCompat.getDrawable(resources, R.drawable.video_selected, null)
        binding.imageOther.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_imp_question, null))
        binding.textOther.setTextColor(ResourcesCompat.getColor(resources, R.color.colorWhite, null))
        binding.textTotalOther.setTextColor(ResourcesCompat.getColor(resources, R.color.colorWhite, null))

        //Important Concepts View
        binding.importantConceptsView.background = ResourcesCompat.getDrawable(resources, R.drawable.not_selected, null)
        binding.textTotalImportantConcepts.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
        binding.imageImportantConcepts.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_concept_default, null))
        binding.textImpConcepts.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
        //Answer Key View
        binding.answerKeyView.background = ResourcesCompat.getDrawable(resources, R.drawable.not_selected, null)
        binding.textTotalAnswerKey.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
        binding.imageAnswerKey.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_answer_key_default, null))
        binding.textAnswerKey.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
        binding.videoView.background = ResourcesCompat.getDrawable(resources, R.drawable.not_selected, null)
        binding.imageVideo.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_video_player_default, null))
        binding.textVideo.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
        binding.textTotalVideo.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))

        //Adhyaya Ka Naam TextView
        binding.tvAdhyayKaNaam.visibility = View.VISIBLE
    }

    private fun getChapters(studentclass:String, studentSubject:String){
        binding.progressBar.visibility = View.VISIBLE
        mRecyclerViewItems.clear()
        if (adapter != null) adapter?.notifyDataSetChanged()

        //Create Retrofit
        val retrofit = Retrofit.Builder().baseUrl("http://completecourse.in/api/").build()

        //Create Service
        val service = retrofit.create(APIService::class.java)

        //Create HashMap with fields
        val params:HashMap<String?, RequestBody?> = HashMap()
        params["studentclass"] = (studentclass).toRequestBody("text/plain".toMediaTypeOrNull())
        params["studentsubject"] = studentSubject.toRequestBody("text/plain".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            //Do the POST request and get the response
            val response = service.postClassAndSubjectCode(params)
            if (response.isSuccessful) {
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
            }
            else{
                Log.e("RETROFIT_ERROR", response.code().toString())
            }

            withContext(Dispatchers.Main){

                binding.progressBar.visibility = View.GONE
                adapter = ClassChaptersAdapter(this@SubjectActivity, mRecyclerViewItems)
                binding.recyclerView.adapter = adapter
                val count = adapter!!.itemCount
                binding.textTotalAnswerKey.text = count.toString()
                binding.textTotalImportantConcepts.text = count.toString()
                binding.textTotalVideo.text = count.toString()
                binding.textTotalOther.text = count.toString()
                binding.recyclerView.addOnItemTouchListener(ClassChaptersAdapter.RecyclerTouchListener(this@SubjectActivity, this@SubjectActivity))
            }
        }
    }

    override fun onClick(view: View) {
        val intent: Intent
        when (view.id) {
            R.id.imgBack -> finish()
            R.id.ic_competition_updates -> {
                intent = Intent(this@SubjectActivity, CompetitionUpdatesActivity::class.java)
                intent.putExtra("class", classString)
                intent.putExtra("subject", subjectString)
                startActivity(intent)
            }

            R.id.nextItmLy -> binding.viewpagerr.currentItem = binding.viewpagerr.currentItem + 1
            R.id.previceItmLy -> binding.viewpagerr.currentItem = binding.viewpagerr.currentItem - 1
        }
    }

    private fun setViewPagerItem(classString: String?, subjectString: String?) {
        if (classString.equals("4") && subjectString.equals("1") ||
                classString.equals("1") && subjectString.equals("2") ||
                classString.equals("2") && subjectString.equals("7") ||
                classString.equals("3") && subjectString.equals("8") ||
                classString.equals("2") && subjectString.equals("18") ||
                classString.equals("3") && subjectString.equals("22")) {
            binding.viewpagerr.currentItem = 0
        } else if (classString.equals("4") && subjectString.equals("16") ||
                classString.equals("1") && subjectString.equals("13") ||
                classString.equals("2") && subjectString.equals("9") ||
                classString.equals("3") && subjectString.equals("10") ||
                classString.equals("2") && subjectString.equals("19") ||
                classString.equals("3") && subjectString.equals("23")) {
            binding.viewpagerr.currentItem = 1
        } else if (classString.equals("2") && subjectString.equals("5") ||
                classString.equals("3") && subjectString.equals("6") ||
                classString.equals("4") && subjectString.equals("4") ||
                classString.equals("1") && subjectString.equals("3") ||
                classString.equals("2") && subjectString.equals("17") ||
                classString.equals("3") && subjectString.equals("21")) {
            binding.viewpagerr.currentItem = 2
        } else if (classString.equals("2") && subjectString.equals("11") ||
                classString.equals("3") && subjectString.equals("12") ||
                classString.equals("4") && subjectString.equals("15") ||
                classString.equals("1") && subjectString.equals("14") ||
                classString.equals("2") && subjectString.equals("20") ||
                classString.equals("3") && subjectString.equals("24")) {
            binding.viewpagerr.currentItem = 3
        }
    }

    companion object {
        var count:Int = 0
        var LOOPS:Int = 0
        var FIRST_PAGE:Int = 0
    }

    private fun clear() {
        val size = mRecyclerViewItems.size
        if (size > 0) {
            mRecyclerViewItems.subList(0, size).clear()
            adapter!!.notifyItemRangeRemoved(0, size)
        }
    }

    init {
        count = 4
        LOOPS = 4
        FIRST_PAGE = 4
    }

    override fun onClick(position: Int) {
        val intent = Intent(this, PDFActivity::class.java)
        val intentVideo = Intent(this, VideoActivity::class.java)
        if (adapter!!.getItemViewType(position) == 0) {
            val chapterItem: ChapterItem = mRecyclerViewItems[position] as ChapterItem
            when {
                binding.answerKeyView.isSelected -> {
                    intent.putExtra("url", chapterItem.chapterKaFlipURL)
                    startActivity(intent)
                }
                binding.importantConceptsView.isSelected -> {
                    intent.putExtra("url", chapterItem.conceptKaFlipURL)
                    startActivity(intent)
                }
                binding.videoView.isSelected -> {
                    intentVideo.putExtra("videoID", chapterItem.chapterKaVideoID)
                    startActivity(intentVideo)
                }
                binding.otherView.isSelected -> {
                    intent.putExtra("url", chapterItem.otherImportantQues)
                    startActivity(intent)
                }
            }
        }
    }
}