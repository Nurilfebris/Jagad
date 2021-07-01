package com.aplikasijagad.API

import com.aplikasijagad.Model.DataAPI
import com.aplikasijagad.models.DataBerhasil
import com.aplikasijagad.models.ResultGagal
import retrofit2.Response
import retrofit2.http.*


interface resiAPI {

    @GET("spb")
    suspend fun getResi(): Response<List<DataAPI>>

    @FormUrlEncoded
    @PUT("ttbretur")
    suspend fun updateGagal(
        @Field("id_amplop") id_amplop: String,
        @Field("mobile_driver_ditolak_alasan") mobile_driver_ditolak_alasan: String,
        @Field("mobile_driver_pengantar") mobile_driver_pengantar: String
    ): Response<ResultGagal>

    @FormUrlEncoded
    @POST("Upload")
    suspend fun updateBerhasil(
        //@PartMap mobile_driver_diterima_foto: Map<String, RequestBody>,
        @Field("mobile_driver_diterima_foto") mobile_driver_diterima_foto: String,
        @Field("id_amplop") id_amplop: String,
        @Field("mobile_driver_diterima_ttd") mobile_driver_diterima_ttd: String,
        @Field("mobile_driver_diterima_nama") mobile_driver_diterima_nama: String,
        @Field("mobile_driver_diterima_jenis_penerima") mobile_driver_diterima_jenis_penerima: String,
        @Field("mobile_driver_pengantar") mobile_driver_pengantar: String,
    ): Response<DataBerhasil>




}