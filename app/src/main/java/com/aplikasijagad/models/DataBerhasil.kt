package com.aplikasijagad.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataBerhasil (
    val status: Boolean,
    val message: String
) :Parcelable