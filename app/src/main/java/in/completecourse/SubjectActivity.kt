package `in`.completecourse

import `in`.completecourse.adapter.CarouselPagerAdapter
import `in`.completecourse.fragment.ClassDetailsFragment
import `in`.completecourse.helper.HelperMethods
import `in`.completecourse.model.Chapter
import `in`.completecourse.utils.APIService
import `in`.completecourse.utils.ListConfig
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_subject.*
import kotlinx.android.synthetic.main.fragment_class_details.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import java.lang.reflect.Type


class SubjectActivity : AppCompatActivity(), View.OnClickListener {
    var classString:String? = null
    var subjectString:String? = null
    var spinPosition:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject)

        findViewById<View>(R.id.nextItmLy).setOnClickListener(this)
        findViewById<View>(R.id.previceItmLy).setOnClickListener(this)
        findViewById<View>(R.id.imgBack).setOnClickListener(this)
        findViewById<View>(R.id.ic_competition_updates).setOnClickListener(this)

        classString = intent.getStringExtra("classCode")
        subjectString = intent.getStringExtra("subjectCode")
        spinPosition = intent.getStringExtra("spinPosition")
        //Log.e("spinPosition", spinPosition)

        viewpagerr.pageMargin = -HelperMethods.getPageMargin(this@SubjectActivity)
        val adapter = CarouselPagerAdapter(this, supportFragmentManager, classString, subjectString)
        viewpagerr.adapter = adapter
        adapter.notifyDataSetChanged()
        viewpagerr.addOnPageChangeListener(adapter)
        viewpagerr.offscreenPageLimit = 3
        setViewPagerItem(classString, subjectString)

        val bundle = Bundle()
        bundle.putString("studentClass", classString)
        bundle.putString("studentSubject", subjectString)
        val classFragment = ClassDetailsFragment()
        classFragment.arguments = bundle

        HelperMethods.loadFragment(classFragment, this@SubjectActivity)

        viewpagerr.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {}
            override fun onPageSelected(i: Int) {
                subjectString = when {
                    classString.equals(ListConfig.classCode[0]) -> { ListConfig.subjectCodeNinth[i % 4] }
                    classString.equals(ListConfig.classCode[1]) -> { ListConfig.subjectCodeTenth[i % 4] }
                    classString.equals(ListConfig.classCode[2]) -> {
                        if (spinPosition.equals("4")){
                            ListConfig.subjectCodeElevenEnglish[i % 4]
                        }else {
                            ListConfig.subjectCodeEleven[i % 4]
                        }
                    }else -> {
                        if (spinPosition.equals("5")){
                            ListConfig.subjectCodeTwelveEnglish[i % 4]
                        }else {
                            ListConfig.subjectCodeTwelve[i % 4]
                        }
                    }
                }
                important_concepts_view.isSelected = false
                answer_key_view.isSelected = true
                answer_key_view.background = ResourcesCompat.getDrawable(resources, R.drawable.video_selected, null)
                image_answer_key.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_answer_key, null))
                text_answer_key.setTextColor(ResourcesCompat.getColor(resources, R.color.colorWhite, null))
                text_total_answer_key.setTextColor(ResourcesCompat.getColor(resources, R.color.colorWhite, null))
                important_concepts_view.background = ResourcesCompat.getDrawable(resources, R.drawable.not_selected, null)
                text_total_important_concepts.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
                image_important_concepts.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_concept_default, null))
                text_imp_concepts.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
                tv_adhyay_ka_naam.visibility = View.VISIBLE
                video_view.background = ResourcesCompat.getDrawable(resources, R.drawable.not_selected, null)
                text_total_video.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
                image_video.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_video_player_default, null))
                text_video.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))

                //Other Important Questions View
                other_view.background = ResourcesCompat.getDrawable(resources, R.drawable.not_selected, null)
                text_total_other.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))
                image_other.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_imp_question_default, null))
                text_other.setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, null))

                /*
                val dataObj = arrayOfNulls<String>(2)
                dataObj[0] = classString
                dataObj[1] = subjectString
                val jsonTransmitter = ClassDetailsFragment.JSONTransmitter(classFragment)
                jsonTransmitter.execute(*dataObj)
                 */

                getChapters(classString!!, subjectString!!)
            }
            override fun onPageScrollStateChanged(i: Int) {}
        })
    }

    private fun getChapters(studentclass:String, studentSubject:String){
        progress_bar.visibility = View.VISIBLE
        //Create Retrofit
        val retrofit = Retrofit.Builder().baseUrl("http://completecourse.in/api/").build()

        //Create Service
        val service = retrofit.create(APIService::class.java)

        //Create HashMap with fields
        val params:HashMap<String?, RequestBody?> = HashMap()
        params["studentclass"] = (studentclass).toRequestBody("text/plain".toMediaTypeOrNull())
        params["studentsubject"] = studentSubject.toRequestBody("text/plain".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                //Do the POST request and get the response
                val response = service.postClassAndSubjectCode(params)
                val obj:JSONObject = JSONObject(response.body()?.string()?:"{}")
                val jsonArray:JSONArray = JSONArray(obj.getJSONArray("data"))
                val listType: Type = object : TypeToken<List<Chapter>>() {}.type
                val yourList: List<Chapter> = Gson().fromJson(jsonArray.toString(), listType)
                withContext(Dispatchers.Main){
                    progress_bar.visibility = View.GONE
                    if (response.isSuccessful){

                    }else{
                        Log.e("RETROFIT_ERROR", response.code().toString())
                    }
                }
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

            R.id.nextItmLy -> viewpagerr.currentItem = viewpagerr.currentItem + 1
            R.id.previceItmLy -> viewpagerr.currentItem = viewpagerr.currentItem - 1
        }
    }

    private fun setViewPagerItem(classString: String?, subjectString: String?) {
        if (classString.equals("4") && subjectString.equals("1") ||
                classString.equals("1") && subjectString.equals("2") ||
                classString.equals("2") && subjectString.equals("7") ||
                classString.equals("3") && subjectString.equals("8") ||
                classString.equals("2") && subjectString.equals("18") ||
                classString.equals("3") && subjectString.equals("22")) {
            viewpagerr.currentItem = 0
        } else if (classString.equals("4") && subjectString.equals("16") ||
                classString.equals("1") && subjectString.equals("13") ||
                classString.equals("2") && subjectString.equals("9") ||
                classString.equals("3") && subjectString.equals("10") ||
                classString.equals("2") && subjectString.equals("19") ||
                classString.equals("3") && subjectString.equals("23")) {
            viewpagerr.currentItem = 1
        } else if (classString.equals("2") && subjectString.equals("5") ||
                classString.equals("3") && subjectString.equals("6") ||
                classString.equals("4") && subjectString.equals("4") ||
                classString.equals("1") && subjectString.equals("3") ||
                classString.equals("2") && subjectString.equals("17") ||
                classString.equals("3") && subjectString.equals("21")) {
            viewpagerr.currentItem = 2
        } else if (classString.equals("2") && subjectString.equals("11") ||
                classString.equals("3") && subjectString.equals("12") ||
                classString.equals("4") && subjectString.equals("15") ||
                classString.equals("1") && subjectString.equals("14") ||
                classString.equals("2") && subjectString.equals("20") ||
                classString.equals("3") && subjectString.equals("24")) {
            viewpagerr.currentItem = 3
        }
    }

    companion object {
        var count:Int = 0
        var LOOPS:Int = 0
        var FIRST_PAGE:Int = 0
    }

    init {
        count = 4
        LOOPS = 4
        FIRST_PAGE = 4
    }
}