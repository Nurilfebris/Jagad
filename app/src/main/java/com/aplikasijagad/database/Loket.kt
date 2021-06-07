package com.aplikasijagad.database

data class Loket(
    //val id: String,
    val nama: String,
    val alamat_asal: String,
    val kecamatan_asal: String,
    val kabupaten_asal: String,
    val provinsi_asal: String,
    val kodepos_asal: String,
    val alamat_tujuan: String,
    val kecamatan_tujuan: String,
    val kabupaten_tujuan: String,
    val provinsi_tujuan: String,
    val kodepos_tujuan: String,
    val berat_barang: String
) {
    constructor() :
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
                ""

                ) {

    }
}

data class Sewa(
    val nama_pengirim_sewa: String,
    val no_Ktp_sewa: String,
    //val kendaraan_sewa: String,
    val hari_sewa: String,
    val deskripsi_hari: String
) {
    constructor() :
            this(
                "",
                "",
                "",
                ""

            )

}

