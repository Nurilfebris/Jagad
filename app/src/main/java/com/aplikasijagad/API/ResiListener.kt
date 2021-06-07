package com.aplikasijagad.API

import android.view.View
import com.aplikasijagad.Model.DataAPI

interface ResiListener {
    fun onResiClicked(view: View, dataAPI: DataAPI)
}