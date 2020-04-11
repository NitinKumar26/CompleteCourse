package `in`.completecourse.fragment

import `in`.completecourse.R
import `in`.completecourse.utils.CarouselLinearLayout
import `in`.completecourse.utils.ListConfig
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class ItemFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        widthAndHeight
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if (container == null) {
            null
        } else inflater.inflate(R.layout.subject_row, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val position = arguments!!.getInt(POSITION)
        val scale = arguments!!.getFloat(SCALE)
        val classString = arguments?.getString(CLASSSTRING)
        val subjectString = arguments?.getString(SUBJECTSTRING)
        val textView = view.findViewById<TextView>(R.id.pager_textview)
        val root: CarouselLinearLayout = view.findViewById(R.id.root_container)
        val imageView = view.findViewById<ImageView>(R.id.imgLogo)

        if (classString.equals("4", ignoreCase = true) || classString.equals("1", ignoreCase = true)) {
            textView.text = ListConfig.subjectHighSchool[position]
            imageView.setImageResource(ListConfig.imagesHighSchool[position])
        } else if (classString.equals("2", ignoreCase = true) || classString.equals("3", ignoreCase = true)) {
            if (subjectString.equals("18") ||
                    subjectString.equals("19") ||
                    subjectString.equals("17") ||
                    subjectString.equals("20") ||
                    subjectString.equals("21") ||
                    subjectString.equals("22") ||
                    subjectString.equals("23") ||
                    subjectString.equals("24")){
                textView.text = ListConfig.subjectIntermediateEnglish[position]
            }else {
                textView.text = ListConfig.subjectIntermediate[position]
            }
            imageView.setImageResource(ListConfig.imagesIntermediate[position])
        }

        root.setScaleBoth(scale)
    }

    /**
     * Get device screen width and height
     */
    private val widthAndHeight: Unit get() {
            val displaymetrics = DisplayMetrics()
            if (activity != null) {
                activity!!.windowManager.defaultDisplay.getMetrics(displaymetrics)
            }
            val screenHeight = displaymetrics.heightPixels
            val screenWidth = displaymetrics.widthPixels
        }

    companion object {
        private const val POSITION = "position"
        private const val SCALE = "scale"
        private const val DRAWABLE_RESOURE = "resource"
        private const val CLASSSTRING = "classString"
        private const val SUBJECTSTRING = "subjectString"


        fun newInstance(context: Context, pos: Int, scale: Float, classString:String?, subjectString:String?): Fragment {
            val b = Bundle()
            b.putInt(POSITION, pos)
            b.putFloat(SCALE, scale)
            b.putString(CLASSSTRING, classString)
            b.putString(SUBJECTSTRING, subjectString)
            return instantiate(context, ItemFragment::class.java.name, b)
        }
    }
}