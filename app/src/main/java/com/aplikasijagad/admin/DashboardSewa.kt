package com.aplikasijagad

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aplikasijagad.master.KendaraanActivity
import kotlinx.android.synthetic.main.fragment_home_sewa.*

class DashboardSewa : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home_sewa)

//        btn_motor.setOnClickListener { launchIntent(Add_Sewa::class.java) }
        //btn_sewaa.setOnClickListener { launchIntent(add_sewa_kendaraan::class.java) }
        btn_kendaraan.setOnClickListener { launchIntent(KendaraanActivity::class.java) }

    }

    @SuppressLint("PrivateResource")
    private fun launchIntent(activity: Class<*>){
        val intent = Intent(applicationContext, activity)
        startActivity(intent)
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
    }
}