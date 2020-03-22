package `in`.completecourse.adapter

import `in`.completecourse.PDFActivity
import `in`.completecourse.R
import `in`.completecourse.model.UpdateItem
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class CompetitionUpdatesAdapter(private val context: Context, list: ArrayList<UpdateItem>?) : RecyclerView.Adapter<CompetitionUpdatesAdapter.UpdatesViewHolder>() {
    private val updateItemsArrayList: ArrayList<UpdateItem> = list!!

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): UpdatesViewHolder {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.competition_update_item, viewGroup, false)
        return UpdatesViewHolder(view)
    }

    override fun onBindViewHolder(updatesViewHolder: UpdatesViewHolder, i: Int) {
        val updateItem: UpdateItem = updateItemsArrayList[i]
        updatesViewHolder.titleText.text = updateItem.updateKaName
        updatesViewHolder.descText.text = updateItem.updateKaDesc
        updatesViewHolder.serialText.text = updateItem.serialNumber

        updatesViewHolder.titleText.setOnClickListener {
            if (updatesViewHolder.descText.visibility == View.GONE) {
                updatesViewHolder.descText.visibility = View.VISIBLE
                updatesViewHolder.knowMoreText.visibility = View.VISIBLE
            } else {
                updatesViewHolder.descText.visibility = View.GONE
                updatesViewHolder.knowMoreText.visibility = View.GONE
            }
        }
        updatesViewHolder.knowMoreText.setOnClickListener { v: View ->
            val intent = Intent(context, PDFActivity::class.java)
            intent.putExtra("url", updateItem.updateKaLink)
            v.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return updateItemsArrayList.size
    }

    inner class UpdatesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serialText: TextView = itemView.findViewById(R.id.text_serial_number)
        val titleText: TextView = itemView.findViewById(R.id.textUpdateTitle)
        val descText: TextView = itemView.findViewById(R.id.desc)
        val knowMoreText: TextView = itemView.findViewById(R.id.know_more)

    }

}