package com.aplikasijagad.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aplikasijagad.API.DataRepository
import com.aplikasijagad.API.Repository
import com.aplikasijagad.Model.APICoronaProv
import com.aplikasijagad.Model.ApiSPB
import com.aplikasijagad.Model.DataAPI
import com.aplikasijagad.models.ResultGagal
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository) : ViewModel() {
    val myResponse: MutableLiveData<Response<List<DataAPI>>> = MutableLiveData()
    val myResponseUpdateGagal: MutableLiveData<Response<ResultGagal>> = MutableLiveData()

    fun getResiviewModel() {
        viewModelScope.launch {
            val response = repository.getResiRepository()
            myResponse.value = response
        }
    }

    fun putUpdateGagalViewModel(
        id_amplop: String,
        mobile_driver_ditolak_alasan: String,
        mobile_driver_pengantar: String
    ) {
        viewModelScope.launch {
            val response = repository.putDataGagal(
                id_amplop,
                mobile_driver_ditolak_alasan,
                mobile_driver_pengantar
            )
            myResponseUpdateGagal.value = response
        }
    }
}