package com.aplikasijagad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.aplikasijagad.admin.DashboardAdmin
import com.aplikasijagad.models.Loket
import com.aplikasijagad.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_loket.*
import kotlinx.android.synthetic.main.activity_signup.*

//class LoketActivity : AppCompatActivity() {
//
//    private lateinit var auth: FirebaseAuth
//    private lateinit var database: DatabaseReference
//    private lateinit var usertype: String
//    private lateinit var uidLoket: String
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_loket)
//
//        auth = FirebaseAuth.getInstance()
//        database = FirebaseDatabase.getInstance().getReference("Loket")
//        uidLoket = database.child("Loket").push().key!!
//
//        add_order()
//    }
//
//    private fun add_order() {
//        val nama_pengirim = ed_nmPengirim.text.toString().trim()
//        val no_pengirim = ed_noPengirim.text.toString().trim()
//        val nama_penerima = ed_nmPenerima.text.toString().trim()
//        val no_penerima = ed_noPenerima.text.toString().trim()
//        val alamat = ed_almtPenerima.text.toString().trim()
//        val berat = ed_beratBarang.text.toString().trim()
//        val harga = ed_harga.text.toString().trim()
//        val status = ed_beratBarang.text.toString().trim()
//        val kurir = ed_beratBarang.text.toString().trim()
//        val waktu = ed_beratBarang.text.toString().trim()
//        val tanggal = ed_beratBarang.text.toString().trim()
//
//        val uid = auth.currentUser!!.uid
//        val data = Loket(
//            uidLoket,
//            nama_pengirim,
//            no_pengirim,
//            nama_penerima,
//            no_penerima,
//            alamat,
//            berat,
//            harga,
//            waktu,
//            tanggal,
//            status,
//            kurir
//        )
//
//        createLoket(data)
//
//        database.child(uid).setValue(data)
//            .addOnFailureListener { e ->
//                Toast.makeText(
//                    this@LoketActivity,
//                    e.toString(),
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//            .addOnSuccessListener {
//                Toast.makeText(
//                    this@LoketActivity,
//                    "Data has been saved",
//                    Toast.LENGTH_LONG
//                ).show()
//                startActivity(Intent(this@LoketActivity, DashboardAdmin::class.java))
//            }
//    }
//
//    private fun createLoket(data: Loket) {
//        database.child("Loket").child(uidLoket).setValue(data)
//    }
//}