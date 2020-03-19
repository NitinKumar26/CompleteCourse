package `in`.completecourse

import `in`.completecourse.SearchActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity(), View.OnClickListener {
    private var spinnerItemsArray: Array<String>
    private var classSelectionSpinner: Spinner? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        title = resources.getString(R.string.app_name)

        //Find the required Views
        findViewById<View>(R.id.layout_physics).setOnClickListener(this)
        findViewById<View>(R.id.layout_chemistry).setOnClickListener(this)
        findViewById<View>(R.id.layout_maths_11_12).setOnClickListener(this)
        findViewById<View>(R.id.layout_biology).setOnClickListener(this)
        findViewById<View>(R.id.science_layout).setOnClickListener(this)
        findViewById<View>(R.id.mathematics_9_10_layout).setOnClickListener(this)
        findViewById<View>(R.id.science_english).setOnClickListener(this)
        findViewById<View>(R.id.math_layout_eng).setOnClickListener(this)
        val mNineTenLayout = findViewById<LinearLayout>(R.id.nineTen)
        val mElevenTwelveLayout = findViewById<LinearLayout>(R.id.elevenTwelve)
        classSelectionSpinner = findViewById(R.id.classSelection)
        spinnerItemsArray = arrayOf("9", "10", "11", "12")
        val spinnerAdapter = SpinAdapter(this@SearchActivity, R.layout.spinner_row, spinnerItemsArray)
        classSelectionSpinner.setAdapter(spinnerAdapter)
        classSelectionSpinner.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                mClassString = spinnerItemsArray[position]
                if (mClassString.equals("9", ignoreCase = true) || mClassString.equals("10", ignoreCase = true)) {
                    mNineTenLayout.visibility = View.VISIBLE
                    mElevenTwelveLayout.visibility = View.GONE
                } else {
                    mElevenTwelveLayout.visibility = View.VISIBLE
                    mNineTenLayout.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
    }

    override fun onClick(view: View) {
        val intent: Intent
        val classString = classSelectionSpinner!!.selectedItem.toString()
        when (view.id) {
            R.id.layout_physics -> {
                intent = Intent(this@SearchActivity, SubjectActivity::class.java)
                if (classString.equals("11", ignoreCase = true)) {
                    intent.putExtra("classCode", ListConfig.classCode.get(2))
                    intent.putExtra("subjectCode", ListConfig.subjectCodeEleven.get(0))
                    startActivity(intent)
                } else {
                    intent.putExtra("classCode", ListConfig.classCode.get(3))
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTwelve.get(0))
                    startActivity(intent)
                }
            }
            R.id.layout_chemistry -> {
                intent = Intent(this@SearchActivity, SubjectActivity::class.java)
                if (classString.equals("11", ignoreCase = true)) {
                    intent.putExtra("classCode", ListConfig.classCode.get(2))
                    intent.putExtra("subjectCode", ListConfig.subjectCodeEleven.get(1))
                    startActivity(intent)
                } else {
                    intent.putExtra("classCode", ListConfig.classCode.get(3))
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTwelve.get(1))
                    startActivity(intent)
                }
            }
            R.id.layout_maths_11_12 -> {
                intent = Intent(this@SearchActivity, SubjectActivity::class.java)
                if (classString.equals("11", ignoreCase = true)) {
                    intent.putExtra("classCode", ListConfig.classCode.get(2))
                    intent.putExtra("subjectCode", ListConfig.subjectCodeEleven.get(2))
                    startActivity(intent)
                } else {
                    intent.putExtra("classCode", ListConfig.classCode.get(3))
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTwelve.get(2))
                    startActivity(intent)
                }
            }
            R.id.layout_biology -> {
                intent = Intent(this@SearchActivity, SubjectActivity::class.java)
                if (classString.equals("11", ignoreCase = true)) {
                    intent.putExtra("classCode", ListConfig.classCode.get(2))
                    intent.putExtra("subjectCode", ListConfig.subjectCodeEleven.get(3))
                    startActivity(intent)
                } else {
                    intent.putExtra("classCode", ListConfig.classCode.get(3))
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTwelve.get(3))
                    startActivity(intent)
                }
            }
            R.id.science_layout -> {
                intent = Intent(this@SearchActivity, SubjectActivity::class.java)
                if (classString.equals("9", ignoreCase = true)) {
                    intent.putExtra("classCode", ListConfig.classCode.get(0))
                    intent.putExtra("subjectCode", ListConfig.subjectCodeNinth.get(0))
                    startActivity(intent)
                } else {
                    intent.putExtra("classCode", ListConfig.classCode.get(1))
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTenth.get(0))
                    startActivity(intent)
                }
            }
            R.id.science_english -> {
                intent = Intent(this@SearchActivity, SubjectActivity::class.java)
                if (classString.equals("9", ignoreCase = true)) {
                    intent.putExtra("classCode", ListConfig.classCode.get(0))
                    intent.putExtra("subjectCode", ListConfig.subjectCodeNinth.get(1))
                    startActivity(intent)
                } else {
                    intent.putExtra("classCode", ListConfig.classCode.get(1))
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTenth.get(1))
                    startActivity(intent)
                }
            }
            R.id.mathematics_9_10_layout -> {
                intent = Intent(this@SearchActivity, SubjectActivity::class.java)
                if (classString.equals("9", ignoreCase = true)) {
                    intent.putExtra("classCode", ListConfig.classCode.get(0))
                    intent.putExtra("subjectCode", ListConfig.subjectCodeNinth.get(2))
                    startActivity(intent)
                } else {
                    intent.putExtra("classCode", ListConfig.classCode.get(1))
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTenth.get(2))
                    startActivity(intent)
                }
            }
            R.id.math_layout_eng -> {
                intent = Intent(this@SearchActivity, SubjectActivity::class.java)
                if (classString.equals("9", ignoreCase = true)) {
                    intent.putExtra("classCode", ListConfig.classCode.get(0))
                    intent.putExtra("subjectCode", ListConfig.subjectCodeNinth.get(3))
                    startActivity(intent)
                } else {
                    intent.putExtra("classCode", ListConfig.classCode.get(1))
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTenth.get(3))
                    startActivity(intent)
                }
            }
        }
    }

    companion object {
        private var mClassString: String? = null
    }
}