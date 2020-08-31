package `in`.completecourse.adapter

import `in`.completecourse.R
import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SpinAdapterNew(context: Context, resource: Int, strArr: Array<String>) : ArrayAdapter<String?>(context, resource, 0, strArr) {
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val mStringArray: Array<String> = strArr
    private val mResource: Int = resource
    private fun homeView(i: Int, viewGroup: ViewGroup): View {
        val inflate = mLayoutInflater.inflate(mResource, viewGroup, false)

        if (i == 0){
            (inflate!!.findViewById<View>(R.id.offer_type_txt) as TextView).text = Html.fromHtml(mStringArray[i])
        }else{
            (inflate!!.findViewById<View>(R.id.offer_type_txt) as TextView).text = Html.fromHtml(mStringArray[i] + "<sup><small>th</small></sup> Class")
        }
        return inflate
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) { convertView = mLayoutInflater.inflate(R.layout.spinner_new_view, parent, false) }
        if (position == 0){ (convertView!!.findViewById<View>(R.id.offer_type_txt) as TextView).text = Html.fromHtml(mStringArray[position])
        }else{ (convertView!!.findViewById<View>(R.id.offer_type_txt) as TextView).text = Html.fromHtml(mStringArray[position] + "<sup><small>th</small></sup> Class") }
        return convertView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return homeView(position, parent)
    }

}