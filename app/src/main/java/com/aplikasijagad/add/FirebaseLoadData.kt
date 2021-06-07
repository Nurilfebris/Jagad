package com.aplikasijagad.add

import com.aplikasijagad.models.Driver

interface FirebaseLoadData {
    fun onFirebaseLoadSuccess(kurirList: List<Driver>)
    fun onFirebaseLoadFailed(message:String)
}