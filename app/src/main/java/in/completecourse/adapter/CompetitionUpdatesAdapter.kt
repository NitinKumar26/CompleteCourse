package `in`.completecourse.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class CompetitionUpdatesAdapter(private val context: Context, list: ArrayList<UpdateItem>) : RecyclerView.Adapter<UpdatesViewHolder>() {
    private val updateItemsArrayList: ArrayList<UpdateItem>
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): UpdatesViewHolder {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.competition_update_item, viewGroup, false)
        return UpdatesViewHolder(view)
    }

    override fun onBindViewHolder(updatesViewHolder: UpdatesViewHolder, i: Int) {
        val updateItem: UpdateItem = updateItemsArrayList[i]
        updatesViewHolder.titleText.setText(updateItem.getUpdateKaName())
        updatesViewHolder.descText.setText(updateItem.getUpdateKaDesc())
        updatesViewHolder.serialText.setText(updateItem.getSerialNumber())

        //updatesViewHolder.titleText.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/Hindi.ttf"));
        updatesViewHolder.titleText.setOnClickListener { v: View? ->
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
            intent.putExtra("url", updateItem.getUpdateKaLink())
            v.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return updateItemsArrayList.size
    }

    internal inner class UpdatesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serialText: TextView
        val titleText: TextView
        val descText: TextView
        val knowMoreText: TextView

        init {
            serialText = itemView.findViewById(R.id.text_serial_number)
            titleText = itemView.findViewById(R.id.textUpdateTitle)
            descText = itemView.findViewById(R.id.desc)
            knowMoreText = itemView.findViewById(R.id.know_more)
        }
    }

    init {
        updateItemsArrayList = list
    }
}