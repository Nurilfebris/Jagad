package com.aplikasijagad.API

import com.aplikasijagad.Model.*
import com.aplikasijagad.models.DataBerhasil
import com.aplikasijagad.models.ResultGagal
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class Repository {

    suspend fun getResiRepository(): Response<List<DataAPI>> {
        return RetrofitInstance.api.getResi()
    }

    suspend fun putDataGagal(
        id_amplop: String,
        mobile_driver_ditolak_alasan: String,
        mobile_driver_pengantar: String
    ): Response<ResultGagal> {
        return RetrofitInstance.api.updateGagal(
            id_amplop,
            mobile_driver_ditolak_alasan,
            mobile_driver_pengantar
        )
    }

    suspend fun putDataBerhasil(
        id_amplop: String,
        mobile_driver_diterima_jenis_penerima: String,
        mobile_driver_pengantar: String,
        mobile_driver_diterima_nama: String,
    ): Response<DataBerhasil> {
        return RetrofitInstance.api.updateSukses(
            id_amplop,
            mobile_driver_diterima_jenis_penerima,
            mobile_driver_pengantar,
            mobile_driver_diterima_nama
        )
    }

    suspend fun putDataSukses(
        //mobile_driver_diterima_foto: Map<String, RequestBody>,
        mobile_driver_diterima_foto: MultipartBody.Part,
        id_amplop: RequestBody,
        mobile_driver_diterima_ttd: MultipartBody.Part,
        mobile_driver_diterima_nama: RequestBody,
        mobile_driver_diterima_jenis_penerima: RequestBody,
        mobile_driver_pengantar: RequestBody
    ) : Response<DataBerhasil> {
        return  RetrofitInstance.api.updateBerhasil(
            mobile_driver_diterima_foto ,
            id_amplop,
            mobile_driver_diterima_ttd ,
            mobile_driver_diterima_nama ,
            mobile_driver_diterima_jenis_penerima ,
            mobile_driver_pengantar
        )
    }
}
