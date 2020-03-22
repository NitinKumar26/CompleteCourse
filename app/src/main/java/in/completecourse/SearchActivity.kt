package `in`.completecourse

import `in`.completecourse.adapter.SpinAdapter
import `in`.completecourse.utils.ListConfig
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        title = resources.getString(R.string.app_name)

        //Find the required Views
        layout_physics.setOnClickListener(this)
        layout_chemistry.setOnClickListener(this)
        layout_maths_11_12.setOnClickListener(this)
        layout_biology.setOnClickListener(this)
        science_layout.setOnClickListener(this)
        mathematics_9_10_layout.setOnClickListener(this)
        science_english.setOnClickListener(this)
        math_layout_eng.setOnClickListener(this)
        val spinnerItemsArray:Array<String> = arrayOf("9", "10", "11", "12")
        val spinnerAdapter = SpinAdapter(this, R.layout.spinner_row, spinnerItemsArray)
        classSelection.adapter = spinnerAdapter
        classSelection.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                mClassString = spinnerItemsArray[position]
                if (mClassString.equals("9", ignoreCase = true) || mClassString.equals("10", ignoreCase = true)) {
                    nineTen.visibility = View.VISIBLE
                    elevenTwelve.visibility = View.GONE
                } else {
                    elevenTwelve.visibility = View.VISIBLE
                    nineTen.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onClick(view: View) {
        val intent: Intent
        val classString = classSelection.selectedItem.toString()
        when (view.id) {
            R.id.layout_physics -> {
                intent = Intent(this@SearchActivity, SubjectActivity::class.java)
                if (classString.equals("11", ignoreCase = true)) {
                    intent.putExtra("classCode", ListConfig.classCode[2])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeEleven[0])
                    startActivity(intent)
                } else {
                    intent.putExtra("classCode", ListConfig.classCode[3])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTwelve[0])
                    startActivity(intent)
                }
            }
            R.id.layout_chemistry -> {
                intent = Intent(this@SearchActivity, SubjectActivity::class.java)
                if (classString.equals("11", ignoreCase = true)) {
                    intent.putExtra("classCode", ListConfig.classCode[2])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeEleven[1])
                    startActivity(intent)
                } else {
                    intent.putExtra("classCode", ListConfig.classCode[3])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTwelve[1])
                    startActivity(intent)
                }
            }
            R.id.layout_maths_11_12 -> {
                intent = Intent(this@SearchActivity, SubjectActivity::class.java)
                if (classString.equals("11", ignoreCase = true)) {
                    intent.putExtra("classCode", ListConfig.classCode[2])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeEleven[2])
                    startActivity(intent)
                } else {
                    intent.putExtra("classCode", ListConfig.classCode[3])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTwelve[2])
                    startActivity(intent)
                }
            }
            R.id.layout_biology -> {
                intent = Intent(this@SearchActivity, SubjectActivity::class.java)
                if (classString.equals("11", ignoreCase = true)) {
                    intent.putExtra("classCode", ListConfig.classCode[2])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeEleven[3])
                    startActivity(intent)
                } else {
                    intent.putExtra("classCode", ListConfig.classCode[3])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTwelve[3])
                    startActivity(intent)
                }
            }
            R.id.science_layout -> {
                intent = Intent(this@SearchActivity, SubjectActivity::class.java)
                if (classString.equals("9", ignoreCase = true)) {
                    intent.putExtra("classCode", ListConfig.classCode[0])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeNinth[0])
                    startActivity(intent)
                } else {
                    intent.putExtra("classCode", ListConfig.classCode[1])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTenth[0])
                    startActivity(intent)
                }
            }
            R.id.science_english -> {
                intent = Intent(this@SearchActivity, SubjectActivity::class.java)
                if (classString.equals("9", ignoreCase = true)) {
                    intent.putExtra("classCode", ListConfig.classCode[0])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeNinth[1])
                    startActivity(intent)
                } else {
                    intent.putExtra("classCode", ListConfig.classCode[1])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTenth[1])
                    startActivity(intent)
                }
            }
            R.id.mathematics_9_10_layout -> {
                intent = Intent(this@SearchActivity, SubjectActivity::class.java)
                if (classString.equals("9", ignoreCase = true)) {
                    intent.putExtra("classCode", ListConfig.classCode[0])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeNinth[2])
                    startActivity(intent)
                } else {
                    intent.putExtra("classCode", ListConfig.classCode[1])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTenth[2])
                    startActivity(intent)
                }
            }
            R.id.math_layout_eng -> {
                intent = Intent(this@SearchActivity, SubjectActivity::class.java)
                if (classString.equals("9", ignoreCase = true)) {
                    intent.putExtra("classCode", ListConfig.classCode[0])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeNinth[3])
                    startActivity(intent)
                } else {
                    intent.putExtra("classCode", ListConfig.classCode[1])
                    intent.putExtra("subjectCode", ListConfig.subjectCodeTenth[3])
                    startActivity(intent)
                }
            }
        }
    }

    companion object {
        private var mClassString: String? = null
    }
}