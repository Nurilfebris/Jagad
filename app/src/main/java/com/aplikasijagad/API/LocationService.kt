package com.aplikasijagad.API

import com.aplikasijagad.models.Location
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface LocationService {
    @Headers("Content-Type: application/json")
    @POST("index.php/api/LokasiDriver")
    fun addLocation(@Body locationData: Location): Call<Location>
    @GET("LokasiDriver")
    fun getLocation(): Call<List<Location>>
}