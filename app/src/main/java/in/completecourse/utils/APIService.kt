package `in`.completecourse.utils

//import com.squareup.okhttp.RequestBody
//import com.squareup.okhttp.ResponseBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PartMap


interface APIService {
    @Multipart
    @POST("chapters.php")
    suspend fun postClassAndSubjectCode(@PartMap params:HashMap<String?, RequestBody?>): Response<ResponseBody>

    /*
    @GET("sizes")
    fun loadSizes(): Call<List<Size?>?>?
     */

}