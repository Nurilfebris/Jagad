package com.aplikasijagad.API

import com.aplikasijagad.Model.*
import com.aplikasijagad.models.ResultGagal
import retrofit2.Response
import java.util.*

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
}
