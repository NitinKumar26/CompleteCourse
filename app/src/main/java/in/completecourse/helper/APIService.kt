package `in`.completecourse.helper

import retrofit2.Call
import retrofit2.http.GET

interface APIService {
    @get:GET("updates")
    val heroes: Call<Any?>?
}