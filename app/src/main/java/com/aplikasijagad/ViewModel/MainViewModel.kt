package com.aplikasijagad.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aplikasijagad.API.Repository
import com.aplikasijagad.Model.DataAPI
import com.aplikasijagad.models.DataBerhasil
import com.aplikasijagad.models.ResultGagal
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository) : ViewModel() {
    val myResponse: MutableLiveData<Response<List<DataAPI>>> = MutableLiveData()
    val myResponseUpdateGagal: MutableLiveData<Response<ResultGagal>> = MutableLiveData()
    val myResponseUpdateSukses: MutableLiveData<Response<DataBerhasil>> = MutableLiveData()

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

    fun putUpdateSuksesViewModel(
        id_amplop: String,
        mobile_driver_diterima_nama: String,
        mobile_driver_diterima_foto: String,
        mobile_driver_diterima_ttd: String,
        mobile_driver_diterima_jenis_penerima: String,
        mobile_driver_pengantar: String
    ) {
        viewModelScope.launch {
            val response = repository.putDataSukses(
                id_amplop,
                mobile_driver_diterima_nama ,
                mobile_driver_diterima_foto ,
                mobile_driver_diterima_ttd ,
                mobile_driver_diterima_jenis_penerima ,
                mobile_driver_pengantar
            )
            myResponseUpdateSukses.value = response
        }
    }
}