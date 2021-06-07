package com.aplikasijagad.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class Users(
    var uid: String = "",
    var profile: String ="",
    var name: String = "",
    var nik: String = "",
    var email: String = "",
    var password: String = "",
    var phone: String = "",
    var address: String = "",
    var usertype: String = "",
    var lat: Double? = null,
    var lng: Double? = null
) {
    constructor() : this(
        "", "", "", "", "", "",
        "", "", "", null, null
    )
}

data class Driver(
    var uid: String = "",
    var name: String = ""
) {
    constructor() : this(
        "",
        ""
    )
}

@Parcelize
data class SURATJALAN(
    var uidSRJ: String ="",
    var driver: String = "",
    var tujuan: String = "",
    var tanggal: String = "",
    var uidDriver: String ="",
    var idAmplop: String ="",
    var berat: String = "",
    var jenisamplop: String = "",
    var noamplop: String = "",
    var penerima: String = "",
    var pengirim: String = "",
    var uidSPB: String =""

): Parcelable {
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "", "",
        "", "","", ""
    )
}
