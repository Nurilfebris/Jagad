package com.aplikasijagad.API

import com.squareup.picasso.OkHttp3Downloader
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataRepository {
    private val client = OkHttpClient.Builder().build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://api.setiatransbudi.com")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
    fun <T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
    fun create(): LocationService{
        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.setiatransbudi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(LocationService::class.java)
    }
}