package com.aplikasijagad

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.aplikasijagad.models.Amplop
import com.aplikasijagad.models.SURATJALAN
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_detail_order.*
import kotlinx.android.synthetic.main.list_amplop.view.*

class DetailOrderActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var listOrder: MutableList<SURATJALAN>
    private lateinit var listAmplop: MutableList<Amplop>
    private lateinit var database: FirebaseDatabase
    private lateinit var adapter: AdapterUtil<Amplop>
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_order)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        listOrder = mutableListOf()
        listAmplop = mutableListOf()
        user = auth.currentUser!!

        val data = intent.getParcelableExtra<SURATJALAN>("data")
        detail_kode.text = data?.uidSRJ
        detail_driver.text = data?.driver
        detail_tanggal.text = data?.tanggal
        detail_tujuan.text = data?.tujuan

        database.getReference("SURATJALAN").child(data!!.uidSRJ).child("Amplop")
            .orderByChild("idSRJ").equalTo(data.uidSRJ).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (eventSnapshot in p0.children) {
                            val data = eventSnapshot.getValue(Amplop::class.java)
                            data?.let {
                                listAmplop.add(it)
                            }
                        }
                    }

                    adapter = AdapterUtil(R.layout.list_amplop , listAmplop , { itemView , item ->
                        itemView.tv_Rincian1.text = item.noamplop
                        itemView.detail_rincian_penerima.text = item.penerima
                        itemView.detail_rincian_pengirim.text = item.pengirim
                        itemView.detail_rincian_alamat.text = item.berat
                        itemView.detail_rincian_jenis.text = item.jenisamplop
                        itemView.detail_rincian_status.text = item.status
                    } , { _ , item ->
                        val intent =
                            Intent(this@DetailOrderActivity , DetailAmplopActivity::class.java)
                        intent.putExtra("data" , item)
                        startActivity(intent)
                    })

                    rvLaporanAmplop.apply {
                        adapter = this@DetailOrderActivity.adapter
                        layoutManager = LinearLayoutManager(context)
                    }
                }
            })
    }
}