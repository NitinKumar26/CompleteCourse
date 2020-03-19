package `in`.completecourse.fragment

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

        //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth / 2, screenHeight / 2);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        assert(this.arguments != null)
        val position = this.arguments!!.getInt(POSITION)
        val scale = this.arguments!!.getFloat(SCALE)
        val textView = view.findViewById<TextView>(R.id.pager_textview)
        val root: CarouselLinearLayout = view.findViewById(R.id.root_container)
        val imageView = view.findViewById<ImageView>(R.id.imgLogo)
        if (SubjectActivity.classString.equals("4", ignoreCase = true) ||
                SubjectActivity.classString.equals("1", ignoreCase = true)) {
            textView.setText(ListConfig.subjectHighSchool.get(position))
            imageView.setImageResource(ListConfig.imagesHighSchool.get(position))
        } else if (SubjectActivity.classString.equals("2", ignoreCase = true) ||
                SubjectActivity.classString.equals("3", ignoreCase = true)) {
            textView.setText(ListConfig.subjectIntermediate.get(position))
            imageView.setImageResource(ListConfig.imagesIntermediate.get(position))
        }
        root.setScaleBoth(scale)
    }

    /**
     * Get device screen width and height
     */
    private val widthAndHeight: Unit
        private get() {
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
        fun newInstance(context: SubjectActivity?, pos: Int, scale: Float): Fragment {
            val b = Bundle()
            b.putInt(POSITION, pos)
            b.putFloat(SCALE, scale)
            return instantiate(context, ItemFragment::class.java.name, b)
        }
    }
}