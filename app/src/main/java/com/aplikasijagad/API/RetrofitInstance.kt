package com.aplikasijagad.API

import android.provider.SyncStateContract
import com.aplikasijagad.Utils.Constants.Companion.BASE_URL
import com.aplikasijagad.Utils.Constants.Companion.BASE_URL4
//import com.aplikasijagad.Model.ResiBarang
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import com.aplikasijagad.Utils.Constants.Companion.BASE_URL5
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL4)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val api: resiAPI by lazy {
        retrofit.create(resiAPI::class.java)
    }

}