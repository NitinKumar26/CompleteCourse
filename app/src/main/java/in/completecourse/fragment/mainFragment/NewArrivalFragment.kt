package `in`.completecourse.fragment.mainFragment

import `in`.completecourse.PDFActivity
import `in`.completecourse.adapter.NewArrivalAdapter
import `in`.completecourse.databinding.FragmentNewArrivalBinding
import `in`.completecourse.helper.HelperMethods
import `in`.completecourse.model.BookNewArrival
import `in`.completecourse.utils.APIService
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Retrofit

class NewArrivalFragment : Fragment(){
    private var mAdapter: NewArrivalAdapter? = null

    //List of quizItems and native ads that populate the RecyclerView;
    private val mRecyclerViewItems: MutableList<Any> = java.util.ArrayList()
    private var _binding: FragmentNewArrivalBinding? = null
    //This property is only valid between onCreateView and
    //onDestroyView
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNewArrivalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = NewArrivalAdapter(view.context, mRecyclerViewItems)

        val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 3)
        binding.recyclerViewStore.layoutManager = mLayoutManager
        binding.recyclerViewStore.itemAnimator = DefaultItemAnimator()
        binding.recyclerViewStore.adapter = mAdapter
        binding.recyclerViewStore.isNestedScrollingEnabled = false

        if (isNetworkAvailable) {
            getNewArrivals()
        } else {
            Toast.makeText(context, "Please check your internet connection.", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Checks if there is Internet accessible.
     * @return True if there is Internet. False if not.
     */
    private val isNetworkAvailable: Boolean get() {
            var activeNetworkInfo: NetworkInfo? = null
            if (activity != null) {
                val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                activeNetworkInfo = connectivityManager.activeNetworkInfo
            }
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    private fun getNewArrivals(){
        binding.progressbarFragmentStore.visibility = View.VISIBLE
        //Create Retrofit
        val retrofit = Retrofit.Builder().baseUrl("http://completecourse.in/api/").build()

        //Create Service
        val service = retrofit.create(APIService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            //Do the POST request and get the response
            val response = service.getNewArrivals()
            if (response.isSuccessful) {
                var bookNewArrival: BookNewArrival
                val obj = JSONObject(response.body()?.string() ?: "{}")
                val jsonArray = obj.getJSONArray("data")//JSONArray(obj.getJSONArray("data"))
                for (i in 0 until jsonArray.length()) {
                    bookNewArrival = BookNewArrival()
                    val c = jsonArray.getJSONObject(i)
                    bookNewArrival.title = c.getString("arrivalkanaam")
                    bookNewArrival.rate = c.getString("arrivalkarate")
                    bookNewArrival.url = c.getString("arrivalkaimageurl")
                    bookNewArrival.siteUrl = c.getString("arrivalkasiteurl")
                    mRecyclerViewItems.add(bookNewArrival)
                }
            }

            withContext(Dispatchers.Main){
                mAdapter?.notifyDataSetChanged()
                binding.progressbarFragmentStore.visibility = View.INVISIBLE
                binding.recyclerViewStore.addOnItemTouchListener(
                    HelperMethods.RecyclerTouchListener(context, object : HelperMethods.ClickListener {
                            override fun onClick(position: Int) {
                                if (mAdapter?.getItemViewType(position) == 0) {
                                    val book: BookNewArrival = mRecyclerViewItems[position] as BookNewArrival
                                    val url: String? = book.siteUrl
                                    val intent = Intent(context, PDFActivity::class.java)
                                    intent.putExtra("url", url)
                                    startActivity(intent)
                                }
                            }
                        })
                )
            }
        }
    }
}