package `in`.completecourse

import `in`.completecourse.adapter.CompetitionUpdatesAdapter
import `in`.completecourse.databinding.ActivityDialogBinding
import `in`.completecourse.model.UpdateItem
import `in`.completecourse.utils.APIService
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import java.io.*
import java.util.*

class CompetitionUpdatesActivity : AppCompatActivity() {
    private var updatesList: ArrayList<UpdateItem>? = null
    private var adapter: CompetitionUpdatesAdapter? = null

    private lateinit var binding: ActivityDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialogBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val classStringFinal = intent.getStringExtra("class")

        if (classStringFinal != null) getCompetitionUpdates(classStringFinal)
    }

    private fun getCompetitionUpdates(classId:String){

        //Create Retrofit
        val retrofit = Retrofit.Builder().baseUrl("http://completecourse.in/api/").build()

        //Create Service
        val service = retrofit.create(APIService::class.java)

        //Create HashMap with fields
        val params:HashMap<String?, RequestBody?> = HashMap()
        params["classid"] = (classId).toRequestBody("text/plain".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            //Do the POST request and get the response
            val response = service.postCompetitionUpdates(params)
            if (response.isSuccessful) {
                var item: UpdateItem
                val obj = JSONObject(response.body()?.string() ?: "{}")
                val jsonArray = obj.getJSONArray("data")//JSONArray(obj.getJSONArray("data"))
                updatesList = ArrayList<UpdateItem>()
                for (i in 0 until jsonArray.length()) {
                    item = UpdateItem()
                    val chapterObject = jsonArray.getJSONObject(i)
                    item.updateKaName = chapterObject.getString("comptkanaam")
                    item.updateKaLink = chapterObject.getString("referencelink")
                    item.updateKaDesc = chapterObject.getString("details")
                    item.serialNumber = (i + 1).toString() + "."
                    updatesList?.add(item)
                }
            }

            else{
                Log.e("RETROFIT_ERROR", response.code().toString())
            }

            withContext(Dispatchers.Main){
                adapter = CompetitionUpdatesAdapter(this@CompetitionUpdatesActivity, updatesList)
                binding.recyclerView.adapter = adapter
            }

        }
    }
}