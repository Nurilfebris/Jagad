package com.aplikasijagad.API

import android.util.Log
import com.aplikasijagad.models.Location
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class RestApiService {
    fun addLocation(locationData: Location , onResult: (Location?)-> Unit){
        val retrofit = DataRepository.buildService(LocationService::class.java)
        retrofit.addLocation(locationData).enqueue(
            object : Callback<Location>{
                override fun onFailure(call: Call<Location> , t: Throwable) {
                    onResult(null)
                }

                override fun onResponse(call: Call<Location> , response: Response<Location>) {
                    val addedLocation = response.body()
                    onResult(addedLocation)
                }
            }
        )
    }
//    fun getLocation(){
//        val locationService = DataRepository.create()
//        locationService.getLocation().enqueue(object : Callback<List<Location>> {
//            override fun onResponse(
//                call: Call<List<Location>> ,
//                response: Response<List<Location>>
//            ) {
//                if (response.isSuccessful){
//                    val data =response.body()
//                    Log.d("hasilGetData", "responsennya ${data?.size}")
//
//                    data?.map {
//                        Log.d("DataHasil", "Latitude: "+it.lat+" Longitude: "+it.long)
//                    }
//                } else{
//                    Log.d("gagalGetLocation","failed")
//                }
//            }
//
//            override fun onFailure(call: Call<List<Location>> , t: Throwable) {
//                Log.d("ErrorGetLocation", "error get data")
//            }
//        } )
//    }
}