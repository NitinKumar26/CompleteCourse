package `in`.completecourse.activity

import `in`.completecourse.adapter.MoreAppsAdapter
import `in`.completecourse.databinding.ActivityMoreAppsBinding
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
import java.util.*

class MoreAppsActivity : AppCompatActivity() {
    private var moreAppsItems: ArrayList<MoreAppsItem>? = null
    var adapter: MoreAppsAdapter? = null
    private lateinit var binding: ActivityMoreAppsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoreAppsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        moreAppsItems = ArrayList<MoreAppsItem>()
        adapter = MoreAppsAdapter(this, moreAppsItems)
        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
        binding.recyclerView.adapter = adapter
        moreApps
        binding.recyclerView.addOnItemTouchListener(HelperMethods.RecyclerTouchListener(this, object : HelperMethods.ClickListener {
            override fun onClick(position: Int) {
                val intent = Intent("android.intent.action.VIEW", Uri.parse(moreAppsItems!![position].app_play_url))
                startActivity(intent)
            }
        }))
    }

    private val moreApps: Unit get() {
            FirebaseFirestore.getInstance()
                .collection("more_apps_cc").get()
                .addOnCompleteListener { binding.progressBar.visibility = View.GONE }
                .addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                        val items: List<MoreAppsItem> = queryDocumentSnapshots.toObjects(MoreAppsItem::class.java)
                        moreAppsItems!!.addAll(items)
                        adapter?.setItems(moreAppsItems)
                        adapter?.notifyDataSetChanged()
                    }.addOnFailureListener { e: Exception -> Toast.makeText(this@MoreAppsActivity, e.message, Toast.LENGTH_LONG).show() }
        }
}