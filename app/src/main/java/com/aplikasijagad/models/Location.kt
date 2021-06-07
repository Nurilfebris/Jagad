package com.aplikasijagad.models

import java.util.*

data class Location(
    val lat: Double ,
    val long: Double ,
    val keterangan: String? ,
    val driver_id: Int?,
    val timestemp: Date?
)