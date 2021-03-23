package `in`.completecourse

import `in`.completecourse.adapter.SpinAdapter
import `in`.completecourse.databinding.ActivityMainBinding
import `in`.completecourse.databinding.ActivitySearchBinding
import `in`.completecourse.utils.ListConfig
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        title = resources.getString(R.string.app_name)

        //Find the required Views
        binding.layoutPhysics.setOnClickListener(this)
        binding.layoutChemistry.setOnClickListener(this)
        binding.layoutMaths1112.setOnClickListener(this)
        binding.layoutBiology.setOnClickListener(this)

        binding.scienceLayout.setOnClickListener(this)
        binding.mathematics910Layout.setOnClickListener(this)
        binding.scienceEnglish.setOnClickListener(this)
        binding.mathLayoutEng.setOnClickListener(this)

        binding.layoutPhysicsEnglish.setOnClickListener(this)
        binding.layoutChemistryEnglish.setOnClickListener(this)
        binding.layoutMaths1112English.setOnClickListener(this)
        binding.layoutBiologyEnglish.setOnClickListener(this)

        val spinnerItemsArray:Array<String> = arrayOf("9", "10", "11", "12")
        val spinnerAdapter = SpinAdapter(this, R.layout.spinner_row, spinnerItemsArray)

        binding.classSelection.adapter = spinnerAdapter

        binding.classSelection.onItemSelectedListener = object : OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                mClassString = spinnerItemsArray[position]

                if (position == 0 || position == 1 ){
                    binding.nineTen.visibility = View.VISIBLE
                    binding.elevenTwelve.visibility = View.GONE
                    binding.elevenTwelveEnglish.visibility = View.GONE
                }else if (position == 2 || position == 3){
                    binding.elevenTwelve.visibility = View.VISIBLE
                    binding.elevenTwelveEnglish.visibility = View.GONE
                    binding.nineTen.visibility = View.GONE
                }else if (position == 4 || position == 5){
                    binding.elevenTwelveEnglish.visibility = View.VISIBLE
                    binding.nineTen.visibility = View.GONE
                    binding.elevenTwelve.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    }

    override fun onClick(view: View) {
        val intent: Intent
        val classString = binding.classSelection.selectedItemPosition
        //Log.e("class", classString.toString())
        when (view.id) {
            R.id.layout_physics -> {
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
                //if (mInterstitialAd!!.isLoaded) mInterstitialAd!!.show()
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
                //if (mInterstitialAd!!.isLoaded) mInterstitialAd!!.show()
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
                //if (mInterstitialAd!!.isLoaded) mInterstitialAd!!.show()
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
                //if (mInterstitialAd!!.isLoaded) mInterstitialAd!!.show()
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
                //if (mInterstitialAd!!.isLoaded) mInterstitialAd!!.show()
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
                ///if (mInterstitialAd!!.isLoaded) mInterstitialAd!!.show()
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
        }
    }

    companion object {
        private var mClassString: String? = null
    }

}