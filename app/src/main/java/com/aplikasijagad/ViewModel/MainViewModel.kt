package com.aplikasijagad.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aplikasijagad.API.Repository
import com.aplikasijagad.Model.DataAPI
import com.aplikasijagad.models.DataBerhasil
import com.aplikasijagad.models.ResultGagal
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class MainViewModel(private val repository: Repository) : ViewModel() {
    val myResponse: MutableLiveData<Response<List<DataAPI>>> = MutableLiveData()
    val myResponseUpdateGagal: MutableLiveData<Response<ResultGagal>> = MutableLiveData()
//    val myResponseUpdateSukses: MutableLiveData<Response<DataBerhasil>> = MutableLiveData()
    val myResponseUpdateBerhasil: MutableLiveData<Response<DataBerhasil>> = MutableLiveData()

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

    fun putUpdateBerhasilViewModel(
        id_amplop: String,
        mobile_driver_diterima_jenis_penerima: String,
        mobile_driver_pengantar: String,
        mobile_driver_diterima_nama: String
    ) {
        viewModelScope.launch {
            val response = repository.putDataBerhasil(
                id_amplop,
                mobile_driver_diterima_jenis_penerima,
                mobile_driver_pengantar,
                mobile_driver_diterima_nama
            )
            myResponseUpdateBerhasil.value = response
        }
    }

//    fun putUpdateSuksesViewModel(
//        mobile_driver_diterima_foto: MultipartBody.Part,
//        id_amplop: RequestBody,
//        mobile_driver_diterima_ttd: MultipartBody.Part,
//        mobile_driver_diterima_nama: RequestBody,
//        mobile_driver_diterima_jenis_penerima: RequestBody,
//        mobile_driver_pengantar: RequestBody
//    ) {
//        viewModelScope.launch {
//            val response = repository.putDataSukses(
//                mobile_driver_diterima_foto ,
//                id_amplop,
//                mobile_driver_diterima_ttd ,
//                mobile_driver_diterima_nama ,
//                mobile_driver_diterima_jenis_penerima ,
//                mobile_driver_pengantar
//            )
//            myResponseUpdateSukses.value = response
//        }
//    }
}