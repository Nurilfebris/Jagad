package com.aplikasijagad

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.aplikasijagad.API.Repository
import com.aplikasijagad.Model.DataAPI
import com.aplikasijagad.ViewModel.MainViewModel
import com.aplikasijagad.ViewModel.MainViewModelFactory
import com.aplikasijagad.auth.LoginActivity
import com.aplikasijagad.models.Amplop
import com.aplikasijagad.models.SURATJALAN
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.accepted.view.*
import kotlinx.android.synthetic.main.activity_detail_amplop.*
import kotlinx.android.synthetic.main.activity_detail_order.*
import kotlinx.android.synthetic.main.activity_diterima.*
import kotlinx.android.synthetic.main.rejected.view.*
import retrofit2.Response
import java.util.*

class DetailAmplopActivity : AppCompatActivity() {

    private lateinit var uri: Uri
    private lateinit var auth: FirebaseAuth
    private lateinit var listAmplop: MutableList<Amplop>
    private lateinit var database: FirebaseDatabase
    private lateinit var user: FirebaseUser
    private lateinit var dropDownText: AutoCompleteTextView

    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null
    private var storageRef: StorageReference? = null


    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_amplop)

        val actionbar = supportActionBar
        actionbar!!.title = "Detail TTB"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        val repository = Repository()
        var isShowPass = false
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storageRef = FirebaseStorage.getInstance().reference.child("Bukti images")
        listAmplop = mutableListOf()
        user = auth.currentUser!!

        // get data disini
        val extra = intent.extras
        if (extra != null) {
            val data = extra.getParcelable<DataAPI>(EXTRA_DATA)

            tv_rincian1.text = data?.id_amplop
            detailPenerima.text = data?.nama_penerima
            detailPengirim.text = data?.nama_pengirim
            detailAlamat.text = data?.alamat_pengirim
            detailTanggal.text = data?.tanggal
            detailStatus.text = data?.status
            if (data!!.status == "2"){
                btn_tolak.setOnClickListener {

                    val builder = AlertDialog.Builder(this)
                    val view = layoutInflater.inflate(R.layout.rejected, null)
                    builder.setView(view)
                    val dialog = builder.show()
                    val ditolak = view.findViewById<EditText>(R.id.penolak).text

                    view.namaDriver.setText(data.nama_driver)

                    view.save_builders.setOnClickListener {
                        updateGagal(
                            id_amplop = data!!.id_amplop,
                            mobile_driver_ditolak_alasan = "Babak Belur",
                            mobile_driver_pengantar = "PIK"
                        )
//                        amplop.child("return").setValue(ditolak.toString())
//                        amplop.child("status").setValue("Return")
//                        dialog.dismiss()
//                        btn_terima.visibility = View.INVISIBLE
//                        btn_tolak.visibility = View.INVISIBLE
                    }

                    view.close_builder.setOnClickListener {
                        dialog.dismiss()
                    }

                }
                btn_terima.setOnClickListener {
                    updateSukses(
                        id_amplop = data!!.id_amplop,
                        mobile_driver_diterima_nama ="Si Alamat C",
                        mobile_driver_diterima_foto ="catur.png",
                        mobile_driver_diterima_ttd ="ctur_ttd.png",
                        mobile_driver_diterima_jenis_penerima ="1",
                        mobile_driver_pengantar="IPIK"
                    )
                }
            }else{
                btn_tolak.visibility=View.GONE
            }



        }


//        if (data?.status == "Return" || data?.status == "Diterima") {
//            btn_terima.visibility = View.INVISIBLE
//            btn_tolak.visibility = View.INVISIBLE
//        } else {
//            btn_terima.visibility = View.VISIBLE
//            btn_tolak.visibility = View.VISIBLE
//        }
//
//        val idSPB = data?.idSPB.toString()
//        val idTTB = data?.nottb.toString()
//        val amplop = database.getReference("SURATJALAN").child(idSPB).child("TTB")
//            .child(idTTB)
//
//
//        btn_terima.setOnClickListener {
//
//            startActivity(Intent(this, DiterimaActivity::class.java))
//
//            val builder = AlertDialog.Builder(this)
//            val view = layoutInflater.inflate(R.layout.accepted, null)
//            builder.setView(view)
//            val dialog = builder.show()
//            val penerima = view.findViewById<EditText>(R.id.penerima).text
//
//            view.save_builder.setOnClickListener {
//                amplop.child("diterima").setValue(penerima.toString())
//                amplop.child("status").setValue("Diterima")
//                dialog.dismiss()
//                btn_terima.visibility = View.INVISIBLE
//                btn_tolak.visibility = View.INVISIBLE
//            }
//
//            view.close_builder.setOnClickListener {
//                dialog.dismiss()
//            }
//        }


//        btn_tolak.setOnClickListener {
//            val builder = AlertDialog.Builder(this)
//            val view = layoutInflater.inflate(R.layout.rejected, null)
//            builder.setView(view)
//            val dialog = builder.show()
//            val ditolak = view.findViewById<EditText>(R.id.penolak).text
//
//            view.save_builders.setOnClickListener {
//                amplop.child("return").setValue(ditolak.toString())
//                amplop.child("status").setValue("Return")
//                dialog.dismiss()
//                btn_terima.visibility = View.INVISIBLE
//                btn_tolak.visibility = View.INVISIBLE
//            }
//
//            view.close_builders.setOnClickListener {
//                dialog.dismiss()
//            }
//        }
    }

    private fun updateGagal(
        id_amplop: String,
        mobile_driver_ditolak_alasan: String,
        mobile_driver_pengantar: String
    ) {
        viewModel.putUpdateGagalViewModel(
            id_amplop,
            mobile_driver_ditolak_alasan,
            mobile_driver_pengantar
        )
        viewModel.myResponseUpdateGagal.observe(this, { response ->
            when {
                response.isSuccessful -> {

                    Log.d("haha", response.body().toString())
                    Log.d("haha", response.code().toString())

                }
            }
        })

    }
    private  fun updateSukses(
        id_amplop: String,
        mobile_driver_diterima_nama : String,
        mobile_driver_diterima_foto : String,
        mobile_driver_diterima_ttd : String,
        mobile_driver_diterima_jenis_penerima : String,
        mobile_driver_pengantar : String
    ){
        viewModel.putUpdateSuksesViewModel(
            id_amplop,
            mobile_driver_diterima_nama ,
            mobile_driver_diterima_foto ,
            mobile_driver_diterima_ttd ,
            mobile_driver_diterima_jenis_penerima ,
            mobile_driver_pengantar
        )
        viewModel.myResponseUpdateSukses.observe(this,{
            response ->
            when{
                response.isSuccessful->{
                    Log.d("hahasukses", response.body().toString())
                    Log.d("hahasukses", response.code().toString())
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



    companion object {
        const val EXTRA_DATA = "extra_data"
    }

}