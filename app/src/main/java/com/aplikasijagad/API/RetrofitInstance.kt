package com.aplikasijagad.API

//import com.aplikasijagad.Model.ResiBarang

import com.aplikasijagad.Utils.Constants.Companion.BASE_URL4
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL4)
            //.addConverterFactory(MoshiConverterFactory.create().asLenient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val api: resiAPI by lazy {
        retrofit.create(resiAPI::class.java)
    }

}