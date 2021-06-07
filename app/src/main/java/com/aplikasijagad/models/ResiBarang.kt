package com.aplikasijagad.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

data class ResiBarang(

    val idResi: String ,
    val tanggal: String ,
    val idPelanggan: String ,
    val idPenerima: String ,
    val beratKirim: String ,
    val jenisKirim: String ,
    val jenisBayar: String ,
    val idTarif: String ,
    val ongkosKirim: String ,
    val ongkosBongkar: String ,

    val diskon: String ,
    val ongkosBersih: String ,
    val bayarAmplop: String ,
    val kembalianAmplop: String

)

data class Berita(
    val title: String ,
    val url: String ,
    val urlToImage: String? ,
    val publishedAt: String?

)

data class coba(
    //val id: Long,
    val name: String ,
    val username: String ,
    val email: String ,
    //val address: Address,
    val phone: String ,
    //val website: String
)

data class sss(
    val idSuratJalan: String ,
    val idAmplop: String ,
//    val tanggal: String,
//    val idDriver: String,
//    val idKendaraan: String,
//    val karyawanID: String,
//    val spvID: String,
//    val namaDriver: String,
//    val nikDriver: String,
//    val hpDriver: String,
//    val namaPengirim: String,
    val alamatPengirim: String
//    val namaPenerima: String,
//    val alamatPenerima: String,
//    val status: String
)
data class Apicorona (
    val name: String,
    val positif: String,
    val sembuh: String,
    val meninggal: String,
    val dirawat: String
)

data class APICoronaProv (
    val FID: Long,
    val Kode_Provi: Long,
    val Provinsi: String,
    val Kasus_Posi: Long,
    val Kasus_Semb: Long,
    val Kasus_Meni: Long,
    val attributes :String
)

data class ApiSPB (
    val status: Boolean,
    val data: List<DataAPI>
)

@Parcelize
data class DataAPI (
    val id_surat_jalan: String,
    val id_amplop: String,
    val tanggal: String,
    val id_driver: String,
    val id_kendaraan: String,
    val karyawan_id: String,
    val spv_id: String,
    val nama_driver: String,
    val nik_driver: String,
    val hp_driver: String,
    val nama_pengirim: String,
    val alamat_pengirim: String,
    val nama_penerima: String,
    val alamat_penerima: String,
    val status: String
): Parcelable