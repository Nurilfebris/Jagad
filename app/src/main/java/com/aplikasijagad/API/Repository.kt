package com.aplikasijagad.API

import com.aplikasijagad.Model.*
import com.aplikasijagad.models.DataBerhasil
import com.aplikasijagad.models.ResultGagal
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

    suspend fun putDataSukses(
        //mobile_driver_diterima_foto: Map<String, RequestBody>,

        id_amplop: String,
        mobile_driver_diterima_nama: String,
        mobile_driver_diterima_foto: String,
        mobile_driver_diterima_ttd: String,
        mobile_driver_diterima_jenis_penerima: String,
        mobile_driver_pengantar: String
    ) : Response<DataBerhasil> {
        return  RetrofitInstance.api.updateBerhasil(
            id_amplop,
            mobile_driver_diterima_nama ,
            mobile_driver_diterima_foto ,
            mobile_driver_diterima_ttd ,
            mobile_driver_diterima_jenis_penerima ,
            mobile_driver_pengantar
        )
    }
}
