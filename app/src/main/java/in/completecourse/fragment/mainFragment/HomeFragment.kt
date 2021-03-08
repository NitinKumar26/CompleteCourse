package `in`.completecourse.fragment.mainFragment

import `in`.completecourse.R
import `in`.completecourse.ScanActivity
import `in`.completecourse.SearchActivity
import `in`.completecourse.SubjectActivity
import `in`.completecourse.adapter.ImageAdapter
import `in`.completecourse.adapter.SliderAdapter
import `in`.completecourse.databinding.FragmentHomeBinding
import `in`.completecourse.helper.HelperMethods
import `in`.completecourse.model.CardModel
import `in`.completecourse.model.Update
import `in`.completecourse.utils.APIService
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Retrofit
import java.util.*

class HomeFragment : Fragment(), ImageAdapter.ClickListener {
    private var updateList: ArrayList<Update>? = null
    private var sliderAdapter: SliderAdapter? = null

    private var _binding: FragmentHomeBinding? = null
    //This property is only valid between onCreateView and
    //onDestroyView
    private val binding get() = _binding!!

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 100 && data != null) {
            urlQR = data.getStringExtra("url")
            if (urlQR != null) {
                val intent = Intent(context, SubjectActivity::class.java)
                when (urlQR) {
                    "http://completecourse.in/api/GetQRValue/4/1" -> {
                        intent.putExtra("classCode", "4")
                        intent.putExtra("subjectCode", "1")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/4/4" -> {
                        intent.putExtra("classCode", "4")
                        intent.putExtra("subjectCode", "4")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/1/2" -> {
                        intent.putExtra("classCode", "1")
                        intent.putExtra("subjectCode", "2")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/1/3" -> {
                        intent.putExtra("classCode", "1")
                        intent.putExtra("subjectCode", "3")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/2/7" -> {
                        intent.putExtra("classCode", "2")
                        intent.putExtra("subjectCode", "7")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/2/9" -> {
                        intent.putExtra("classCode", "2")
                        intent.putExtra("subjectCode", "9")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/2/11" -> {
                        intent.putExtra("classCode", "2")
                        intent.putExtra("subjectCode", "11")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/2/5" -> {
                        intent.putExtra("classCode", "2")
                        intent.putExtra("subjectCode", "5")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/3/8" -> {
                        intent.putExtra("classCode", "3")
                        intent.putExtra("subjectCode", "8")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/3/10" -> {
                        intent.putExtra("classCode", "3")
                        intent.putExtra("subjectCode", "10")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/3/12" -> {
                        intent.putExtra("classCode", "3")
                        intent.putExtra("subjectCode", "12")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/3/6" -> {
                        intent.putExtra("classCode", "3")
                        intent.putExtra("subjectCode", "6")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/4/16" -> {
                        intent.putExtra("classCode", "4")
                        intent.putExtra("subjectCode", "16")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/4/15" -> {
                        intent.putExtra("classCode", "4")
                        intent.putExtra("subjectCode", "15")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/1/13" -> {
                        intent.putExtra("classCode", "1")
                        intent.putExtra("subjectCode", "15")
                        startActivity(intent)
                    }
                    "http://completecourse.in/api/GetQRValue/1/14" -> {
                        intent.putExtra("classCode", "1")
                        intent.putExtra("subjectCode", "14")
                        startActivity(intent)
                    }
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val cardList: ArrayList<CardModel> = ArrayList<CardModel>()
        cardList.add(CardModel(R.drawable.manual_search, "Manual Search"))
        cardList.add(CardModel(R.drawable.scan_qr, "Scan QR Code"))

        binding.recyclerView.setHasFixedSize(true)
        //use a linear layout manager
        val gridLayoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        binding.recyclerView.layoutManager = gridLayoutManager

        //specify an adapter
        val recyclerViewAdapter = ImageAdapter(cardList, context!!)
        binding.recyclerView.adapter = recyclerViewAdapter
        binding.recyclerView.addOnItemTouchListener(ImageAdapter.RecyclerTouchListener(context, this))

        val timer = Timer()
        timer.scheduleAtFixedRate(SliderTimer(), 4000, 6000)
        if (HelperMethods.isNetworkAvailable(activity)) getUpdates()
        else Toast.makeText(view.context, "Please check your internet connection.", Toast.LENGTH_SHORT).show()
    }

    private inner class SliderTimer : TimerTask() {
        override fun run() {
            if (activity != null) {
                activity!!.runOnUiThread {
                    if (binding.viewPager.currentItem < updateList?.size?.minus(1) ?: 0) {
                        binding.viewPager.currentItem = binding.viewPager.currentItem + 1
                    } else {
                        binding.viewPager.currentItem = 0
                    }
                }
            }
        }
    }

    private fun getUpdates() {
        //binding.progressBar.visibility = View.VISIBLE
        //Create Retrofit
        val retrofit = Retrofit.Builder().baseUrl("http://completecourse.in/api/").build()

        //Create Service
        val service = retrofit.create(APIService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            //Do the POST request and get the response
            val response = service.getUpdates()
            if (response.isSuccessful) {
                var update: Update
                val obj = JSONObject(response.body()?.string() ?: "{}")
                val jsonArray = obj.getJSONArray("data")//JSONArray(obj.getJSONArray("data"))
                //Log.e("jsonArray", jsonArray.toString())
                for (i in 0 until jsonArray.length()) {
                    val dataObject = jsonArray.getJSONObject(i)
                    update = Update(dataObject.getString("name"), dataObject.getString("imageurl"))
                    updateList = ArrayList()
                    updateList?.add(update)
                }
            }

            withContext(Dispatchers.Main) {
                sliderAdapter = SliderAdapter(activity, updateList!!)
                sliderAdapter?.setItems(updateList)
                binding.indicator.setupWithViewPager(binding.viewPager, true)
                binding.viewPager.adapter = sliderAdapter
            }
        }
    }

    companion object {
        private var urlQR: String? = null
    }

    override fun onClick(position: Int) {
        when (position) {
            0 -> {
                val searchActivityIntent = Intent(context, SearchActivity::class.java)
                startActivity(searchActivityIntent)
            }
            1 -> {
                val qrCodeActivityIntent = Intent(context, ScanActivity::class.java)
                startActivityForResult(qrCodeActivityIntent, 100)
            }
        }
    }
}