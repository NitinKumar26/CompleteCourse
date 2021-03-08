package `in`.completecourse.fragment.mainFragment

import `in`.completecourse.PDFActivity
import `in`.completecourse.adapter.NotificationAdapter
import `in`.completecourse.databinding.FragmentNotificationBinding
import `in`.completecourse.helper.HelperMethods
import `in`.completecourse.model.NotificationModel
import `in`.completecourse.utils.APIService
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Retrofit
import java.util.*

class NotificationFragment : Fragment(), NotificationAdapter.ClickListener {
    private var itemsList: ArrayList<NotificationModel>? = null
    private var mAdapter: NotificationAdapter? = null

    private var _binding: FragmentNotificationBinding? = null
    //This property is only valid between onCreateView and
    //onDestroyView
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemsList = ArrayList<NotificationModel>()
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)

        binding.recyclerViewNotification.layoutManager = mLayoutManager
        binding.recyclerViewNotification.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))
        binding.recyclerViewNotification.isNestedScrollingEnabled = false

        if (HelperMethods.isNetworkAvailable(activity)) {
            getNotifications()
        } else {
            Toast.makeText(context, "Please check your internet connection.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getNotifications(){
        binding.progressBar.visibility = View.VISIBLE
        //Create Retrofit
        val retrofit = Retrofit.Builder().baseUrl("http://completecourse.in/api/").build()

        //Create Service
        val service = retrofit.create(APIService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            //Do the POST request and get the response
            val response = service.getNotifications()
            if (response.isSuccessful) {
                var model: NotificationModel
                val obj = JSONObject(response.body()?.string() ?: "{}")
                val jsonArray = obj.getJSONArray("data")//JSONArray(obj.getJSONArray("data"))
                //Log.e("jsonArray", jsonArray.toString())
                for (i in 0 until jsonArray.length()) {
                    model = NotificationModel()
                    val c = jsonArray.getJSONObject(i)
                    model.mHeading = c.getString("notifyheading")
                    model.mSubHeading = c.getString("notifydetails")
                    model.url = c.getString("notifyURL")
                    model.serial = (i + 1).toString() + ". "
                    itemsList?.add(model)
                }
            }

            withContext(Dispatchers.Main){
                binding.progressBar.visibility = View.GONE
                if (itemsList!!.isEmpty()) {
                    binding.emptyLayout.visibility = View.VISIBLE
                }
                mAdapter = NotificationAdapter(activity!!, itemsList!!)
                binding.recyclerViewNotification.adapter = mAdapter
                binding.recyclerViewNotification.addOnItemTouchListener(NotificationAdapter.RecyclerTouchListener(context, this@NotificationFragment))
            }
        }
    }

    override fun onClick(position: Int) {
        val url: String? = itemsList!![position].url
        val intent = Intent(activity, PDFActivity::class.java)
        intent.putExtra("url", url)
        startActivity(intent)
    }
}