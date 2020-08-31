package `in`.completecourse

import `in`.completecourse.adapter.SpinAdapter
import `in`.completecourse.utils.ListConfig
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity(), View.OnClickListener {
    private var mInterstitialAd: InterstitialAd? = null
    private var adRequest: AdRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        title = resources.getString(R.string.app_name)

        adRequest = AdRequest.Builder().build()
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd!!.adUnitId = getString(R.string.interstitial_ad_id)
        mInterstitialAd!!.loadAd(adRequest)


        //Find the required Views
        layout_physics.setOnClickListener(this)
        layout_chemistry.setOnClickListener(this)
        layout_maths_11_12.setOnClickListener(this)
        layout_biology.setOnClickListener(this)

        science_layout.setOnClickListener(this)
        mathematics_9_10_layout.setOnClickListener(this)
        science_english.setOnClickListener(this)
        math_layout_eng.setOnClickListener(this)

        layout_physicsEnglish.setOnClickListener(this)
        layout_chemistryEnglish.setOnClickListener(this)
        layout_maths_11_12English.setOnClickListener(this)
        layout_biologyEnglish.setOnClickListener(this)

        val spinnerItemsArray:Array<String> = arrayOf("9", "10", "11", "12", "11", "12")
        val spinnerAdapter = SpinAdapter(this, R.layout.spinner_row, spinnerItemsArray)

        classSelection.adapter = spinnerAdapter

        classSelection.onItemSelectedListener = object : OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                mClassString = spinnerItemsArray[position]

                if (position == 0 || position == 1 ){
                    nineTen.visibility = View.VISIBLE
                    elevenTwelve.visibility = View.GONE
                    elevenTwelveEnglish.visibility = View.GONE
                }else if (position == 2 || position == 3){
                    elevenTwelve.visibility = View.VISIBLE
                    elevenTwelveEnglish.visibility = View.GONE
                    nineTen.visibility = View.GONE
                }else if (position == 4 || position == 5){
                    elevenTwelveEnglish.visibility = View.VISIBLE
                    nineTen.visibility = View.GONE
                    elevenTwelve.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    }

    override fun onClick(view: View) {
        val intent: Intent
        val classString = classSelection.selectedItemPosition
        //Log.e("class", classString.toString())
        when (view.id) {
            R.id.layout_physics -> {
                if (mInterstitialAd!!.isLoaded) mInterstitialAd!!.show()
                intent = Intent(this@SearchActivity, SubjectActivity::class.java)
                if (classString == 2) {
                    intent.putExtra("classCode", ListConfig.classCode[2])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeEleven[0])
                    intent.putExtra("spinPosition", classString.toString())
                    startActivity(intent)
                } else {
                    intent.putExtra("classCode", ListConfig.classCode[3])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTwelve[0])
                    intent.putExtra("spinPosition", classString.toString())
                    startActivity(intent)
                }
            }
            R.id.layout_chemistry -> {
                if (mInterstitialAd!!.isLoaded) mInterstitialAd!!.show()
                intent = Intent(this@SearchActivity, SubjectActivity::class.java)
                if (classString == 2) {
                    intent.putExtra("classCode", ListConfig.classCode[2])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeEleven[1])
                    intent.putExtra("spinPosition", classString.toString())
                    startActivity(intent)
                } else {
                    intent.putExtra("classCode", ListConfig.classCode[3])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTwelve[1])
                    intent.putExtra("spinPosition", classString.toString())
                    startActivity(intent)
                }
            }
            R.id.layout_maths_11_12 -> {
                if (mInterstitialAd!!.isLoaded) mInterstitialAd!!.show()
                intent = Intent(this@SearchActivity, SubjectActivity::class.java)
                if (classString == 2) {
                    intent.putExtra("classCode", ListConfig.classCode[2])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeEleven[2])
                    intent.putExtra("spinPosition", classString.toString())
                    startActivity(intent)
                } else {
                    intent.putExtra("classCode", ListConfig.classCode[3])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTwelve[2])
                    intent.putExtra("spinPosition", classString.toString())
                    startActivity(intent)
                }
            }
            R.id.layout_biology -> {
                if (mInterstitialAd!!.isLoaded) mInterstitialAd!!.show()
                intent = Intent(this@SearchActivity, SubjectActivity::class.java)
                if (classString ==  2) {
                    intent.putExtra("classCode", ListConfig.classCode[2])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeEleven[3])
                    intent.putExtra("spinPosition", classString.toString())
                    startActivity(intent)
                } else {
                    intent.putExtra("classCode", ListConfig.classCode[3])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTwelve[3])
                    intent.putExtra("spinPosition", classString.toString())
                    startActivity(intent)
                }
            }
            R.id.science_layout -> {
                if (mInterstitialAd!!.isLoaded) mInterstitialAd!!.show()
                intent = Intent(this@SearchActivity, SubjectActivity::class.java)
                if (classString == 0) {
                    intent.putExtra("classCode", ListConfig.classCode[0])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeNinth[0])
                    intent.putExtra("spinPosition", classString.toString())
                    startActivity(intent)
                } else {
                    intent.putExtra("classCode", ListConfig.classCode[1])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTenth[0])
                    intent.putExtra("spinPosition", classString.toString())
                    startActivity(intent)
                }
            }
            R.id.science_english -> {
                if (mInterstitialAd!!.isLoaded) mInterstitialAd!!.show()
                intent = Intent(this@SearchActivity, SubjectActivity::class.java)
                if (classString == 0) {
                    intent.putExtra("classCode", ListConfig.classCode[0])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeNinth[1])
                    intent.putExtra("spinPosition", classString.toString())
                    startActivity(intent)
                } else {
                    intent.putExtra("classCode", ListConfig.classCode[1])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTenth[1])
                    intent.putExtra("spinPosition", classString.toString())
                    startActivity(intent)
                }
            }
            R.id.mathematics_9_10_layout -> {
                if (mInterstitialAd!!.isLoaded) mInterstitialAd!!.show()
                intent = Intent(this@SearchActivity, SubjectActivity::class.java)
                if (classString == 0) {
                    intent.putExtra("classCode", ListConfig.classCode[0])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeNinth[2])
                    intent.putExtra("spinPosition", classString.toString())
                    startActivity(intent)
                } else {
                    intent.putExtra("classCode", ListConfig.classCode[1])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTenth[2])
                    intent.putExtra("spinPosition", classString.toString())
                    startActivity(intent)
                }
            }
            R.id.math_layout_eng -> {
                if (mInterstitialAd!!.isLoaded) mInterstitialAd!!.show()
                intent = Intent(this@SearchActivity, SubjectActivity::class.java)
                if (classString == 0) {
                    intent.putExtra("classCode", ListConfig.classCode[0])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeNinth[3])
                    intent.putExtra("spinPosition", classString.toString())
                    startActivity(intent)
                } else {
                    intent.putExtra("classCode", ListConfig.classCode[1])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTenth[3])
                    intent.putExtra("spinPosition", classString.toString())
                    startActivity(intent)
                }
            }
            R.id.layout_physicsEnglish -> {
                if (mInterstitialAd!!.isLoaded) mInterstitialAd!!.show()
                intent = Intent(this@SearchActivity, SubjectActivity::class.java)
                if (classString == 4) {
                    intent.putExtra("classCode", ListConfig.classCode[2])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeElevenEnglish[0])
                    intent.putExtra("spinPosition", classString.toString())
                    startActivity(intent)
                } else {
                    intent.putExtra("classCode", ListConfig.classCode[3])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTwelveEnglish[0])
                    intent.putExtra("spinPosition", classString.toString())
                    startActivity(intent)
                }
            }
            R.id.layout_chemistryEnglish -> {
                if (mInterstitialAd!!.isLoaded) mInterstitialAd!!.show()
                intent = Intent(this@SearchActivity, SubjectActivity::class.java)
                if (classString == 4) {
                    intent.putExtra("classCode", ListConfig.classCode[2])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeElevenEnglish[1])
                    intent.putExtra("spinPosition", classString.toString())
                    startActivity(intent)
                } else {
                    intent.putExtra("classCode", ListConfig.classCode[3])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTwelveEnglish[1])
                    intent.putExtra("spinPosition", classString.toString())
                    startActivity(intent)
                }
            }
            R.id.layout_maths_11_12English -> {
                if (mInterstitialAd!!.isLoaded) mInterstitialAd!!.show()
                intent = Intent(this@SearchActivity, SubjectActivity::class.java)
                if (classString == 4) {
                    intent.putExtra("classCode", ListConfig.classCode[2])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeElevenEnglish[2])
                    intent.putExtra("spinPosition", classString.toString())
                    startActivity(intent)
                } else {
                    intent.putExtra("classCode", ListConfig.classCode[3])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTwelveEnglish[2])
                    intent.putExtra("spinPosition", classString.toString())
                    startActivity(intent)
                }
            }
            R.id.layout_biologyEnglish -> {
                if (mInterstitialAd!!.isLoaded) mInterstitialAd!!.show()
                intent = Intent(this@SearchActivity, SubjectActivity::class.java)
                if (classString == 4) {
                    intent.putExtra("classCode", ListConfig.classCode[2])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeElevenEnglish[3])
                    intent.putExtra("spinPosition", classString.toString())
                    startActivity(intent)
                } else {
                    intent.putExtra("classCode", ListConfig.classCode[3])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTwelveEnglish[3])
                    intent.putExtra("spinPosition", classString.toString())
                    startActivity(intent)
                }
            }
        }
    }

    companion object {
        private var mClassString: String? = null
    }

}