package `in`.completecourse.activity

import `in`.completecourse.R
import `in`.completecourse.adapter.MoreAppsAdapter
import `in`.completecourse.helper.HelperMethods
import `in`.completecourse.model.MoreAppsItem
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_more_apps.*
import java.util.*

class MoreAppsActivity : AppCompatActivity() {
    private var moreAppsItems: ArrayList<MoreAppsItem>? = null
    var adapter: MoreAppsAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_apps)
        moreAppsItems = ArrayList<MoreAppsItem>()
        adapter = MoreAppsAdapter(this, moreAppsItems)
        recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
        recycler_view.adapter = adapter
        moreApps
        recycler_view.addOnItemTouchListener(HelperMethods.RecyclerTouchListener(this, object : HelperMethods.ClickListener {
            override fun onClick(position: Int) {
                val intent = Intent("android.intent.action.VIEW", Uri.parse(moreAppsItems!![position].app_play_url))
                startActivity(intent)
            }
        }))
    }

    private val moreApps: Unit get() {
            FirebaseFirestore.getInstance().collection("more_apps_cc").get().addOnCompleteListener { progressBar!!.visibility = View.GONE }.addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                        val items: List<MoreAppsItem> = queryDocumentSnapshots.toObjects(MoreAppsItem::class.java)
                        moreAppsItems!!.addAll(items)
                        adapter?.setItems(moreAppsItems)
                        adapter?.notifyDataSetChanged()
                    }.addOnFailureListener { e: Exception -> Toast.makeText(this@MoreAppsActivity, e.message, Toast.LENGTH_LONG).show() }
        }
}