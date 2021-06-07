package com.aplikasijagad.master

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.aplikasijagad.R
import com.aplikasijagad.databinding.ActivityKendaraanBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_kendaraan.*


class KendaraanActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityKendaraanBinding
    lateinit var ref: DatabaseReference
    private lateinit var idKendaraan: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kendaraan)
        idKendaraan = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().getReference("Kendaraan")
        btn_formKendaraan.setOnClickListener {
            when {
                ed_namaKendaraan.text.isEmpty() -> {
                    ed_namaKendaraan.error = "Nama Kendaraan harus diisi"
                    return@setOnClickListener
                }
                ed_warna.text.isEmpty()->{
                    ed_warna.error="Warna harus diisi"
                    return@setOnClickListener
                }ed_noPolisi.text.isEmpty()->{
                    ed_noPolisi.error="wajib diisi"
                return@setOnClickListener
            }
                else->{
                //saveDataKendaraan()

            }
            }
        }
//        onItemSelectedkendaraan()
    }

//    private fun saveDataKendaraan() {
//       val idKendaraan = idKendaraan.currentUser!!.uid
//        val namaKendaraan = ed_namaKendaraan.text.toString()
//        val warnaKendaraan = ed_warna.text.toString()
//        val noPolisi = ed_noPolisi.text.toString()
//        val jenis = binding.spinJenis.selectedItemId.toString()
//
//        val kendaraan = Kendaraan(
//            kendaraanid,
//            namaKendaraan,
//            warnaKendaraan,
//            noPolisi,
//            jenis
//        )
//        val kendaraanid = ref.push().key.toString()
//        ref.child(kendaraanid).setValue(kendaraan).addOnCompleteListener {
//            Toast.makeText(this, "data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
//            ed_namaKendaraan.setText("")
//            ed_warna.setText("")
//            ed_noPolisi.setText("")
//            binding.spinJenis.selectedItem
//
//        }
//    }
//    private fun onItemSelectedkendaraan() {
//        val option = binding.spinJenis
//        val options = arrayOf( "Motor", "Mobil")
//
//        option.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options)
//        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onNothingSelected(p0: AdapterView<*>?) {
////                if (binding.spinner2.selectedItem==null){
////                    Toast.makeText(getApplicationContext(),"data pilih kendaraan kosong",Toast.LENGTH_SHORT).show()
////                }
//            }
//
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: android.view.View?,
//                position: Int,
//                id: Long
//            ) {

//                if (options[position] == "motor") {
//                    val item = parent?.getItemAtPosition(position).toString()
//                    status = item
//                }
//                if (options[position] == "mobil") {
//                    val item = parent?.getItemAtPosition(position).toString()
//                    status = item
//                }
            }

//        }
//
//    }
//}