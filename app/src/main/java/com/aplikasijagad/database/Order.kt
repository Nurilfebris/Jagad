package com.aplikasijagad.database

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    val uidorder:String = "",
    val uiuser: String,
    val namaPengirim: String= "",
    val noPengirim: String = "",
    val namaPenerima: String = "",
    val noPenerima: String = "",
    val alamat: String = "",
    val berat:String = "",
    val harga:String = "",
    val status:String = "",
    val kurir: String = "",
    val tanggal:String ="",
    val waktu:String=""

):Parcelable{
    constructor():
            this(
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""

            )
}