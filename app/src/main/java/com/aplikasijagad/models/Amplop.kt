package com.aplikasijagad.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Amplop(
    var idSRJ: String ="",
    var uidDriver: String="",
    var berat: String = "",
    var jenisamplop: String = "",
    var noamplop: String = "",
    var penerima: String = "",
    var pengirim: String = "",
    var status: String = "",
    var ditolak: String = "",
    var diterima: String = "",
    var pilihpenerima: String= "",
    var imageUrl: String= "",
    var jenisttb: String= "",
    var idSPB: String= "",
    var nottb: String= ""

): Parcelable  {
    constructor() : this(
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
        "",
        "",
        ""
    )
}
