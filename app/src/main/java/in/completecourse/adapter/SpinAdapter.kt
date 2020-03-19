package `in`.completecourse.adapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SpinAdapter(context: Context, resource: Int, strArr: Array<String>) : ArrayAdapter<String?>(context, resource, 0, strArr) {
    private val mLayoutInflater: LayoutInflater
    private val mStringArray: Array<String>
    private val mResource: Int
    private fun homeView(i: Int, viewGroup: ViewGroup): View {
        val inflate = mLayoutInflater.inflate(mResource, viewGroup, false)
        (inflate.findViewById<View>(R.id.offer_type_txt) as TextView).text = Html.fromHtml(mStringArray[i] +
                "<sup><small>th</small></sup> Class")
        return inflate
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.spinner_new_view, parent, false)
        }
        (convertView!!.findViewById<View>(R.id.offer_type_txt) as TextView).text = Html.fromHtml(mStringArray[position] + "<sup><small>th</small></sup> Class")
        return convertView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return homeView(position, parent)
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mResource = resource
        mStringArray = strArr
    }
}