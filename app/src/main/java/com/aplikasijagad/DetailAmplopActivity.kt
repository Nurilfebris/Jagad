package com.aplikasijagad

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.aplikasijagad.API.Repository
import com.aplikasijagad.Model.DataAPI
import com.aplikasijagad.ViewModel.MainViewModel
import com.aplikasijagad.ViewModel.MainViewModelFactory
import com.aplikasijagad.models.Amplop
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_detail_amplop.*
import kotlinx.android.synthetic.main.rejected.*
import kotlinx.android.synthetic.main.rejected.view.*
import java.io.*
import kotlin.jvm.Throws

class DetailAmplopActivity : AppCompatActivity() {

    private lateinit var uri: Uri
    private lateinit var auth: FirebaseAuth
    private lateinit var listAmplop: MutableList<Amplop>
    private lateinit var database: FirebaseDatabase
    private lateinit var user: FirebaseUser
    private lateinit var dropDownText: AutoCompleteTextView
    private lateinit var textInputLayout: TextInputLayout
    lateinit var sharedPreferences: SharedPreferences

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

        //shared prerenced
        sharedPreferences = getSharedPreferences(ARGS_ROLE, Context.MODE_PRIVATE)
        val repository = Repository()
        var isShowPass = false
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storageRef = FirebaseStorage.getInstance().reference.child("Bukti images")
        listAmplop = mutableListOf()
        user = auth.currentUser!!

        //set permission
        verifyStoragePermissions(this);

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

                    view.namaDriver.setText(data.nama_driver)

                    view.save_builders.setOnClickListener {
                        Log.d("coba",view.namaDriver.text.toString())
                        Log.d("coba",view.penolak.text.toString())

                        updateGagal(
                            id_amplop = data!!.id_amplop,
                            mobile_driver_ditolak_alasan = view.penolak.text.toString(),
                            mobile_driver_pengantar = data!!.nama_driver
                        )
                    }

                    view.close_builders.setOnClickListener {
                        dialog.dismiss()
                    }

                }

                btn_terima.setOnClickListener {

                    val editorSaved: SharedPreferences.Editor = sharedPreferences.edit()
                    editorSaved.putString(ARGS_ROLE, data.id_amplop)
                    editorSaved.putString(ARGS_ROLE, data.nama_driver)
                    editorSaved.apply()
                    Log.d("asa",data.id_amplop)
                    startActivity(Intent(this, DiterimaActivity::class.java))

                }

            }else{
                btn_tolak.visibility=View.GONE
            }
        }

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
                    Log.d("bebas", penolak.text.toString())
                    Log.d("haha", response.body().toString())
                    Log.d("haha", response.code().toString())

                }
            }
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun dropDown() {
        textInputLayout = findViewById(R.id.spinStatus)
        dropDownText = findViewById(R.id.dropdown_text)

        val items = arrayOf(
            "Satpam",
            "Teman Kantor",
            "Temen Kerja",
            "Temen Dekat",
            "Sahabat Selamanya",
            "Orang Rumah"
        )

        val adapter = ArrayAdapter(
            this,
            R.layout.dropddown_item,
            items
        )

        dropDownText.setAdapter(adapter)
        dropDownText.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
                val category = items[p2]
                dropDownText.setText(category)
            }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    fun getAlbumStorageDir(albumName: String?): File {
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            albumName
        )
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created")
        }
        return file
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == DetailAmplopActivity.REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.size <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this, "Tidak dapat menulis gambar ke Media Penyimpanan",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    //Image Signature
    @Throws(IOException::class)
    fun saveBitmapToJPG(bitmap: Bitmap, photo: File?) {
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        val stream: OutputStream = FileOutputStream(photo)
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        stream.close()
    }

    fun addJpgSignatureToGallery(signature: Bitmap): Boolean {
        var result = false
        try {
            val photo = File(
                getAlbumStorageDir("SignaturePad"), String.format(
                    "Signature_%d.jpg",
                    System.currentTimeMillis()
                )
            )
            saveBitmapToJPG(signature, photo)
            scanMediaFile(photo)
            result = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }

    private fun scanMediaFile(photo: File) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri = Uri.fromFile(photo)
        mediaScanIntent.data = contentUri
        this.sendBroadcast(mediaScanIntent)
    }

    //SVG Signature
    fun addSvgSignatureToGallery(signatureSvg: String?): Boolean {
        var result = false
        try {
            val svgFile = File(
                getAlbumStorageDir("SignaturePad"), String.format(
                    "Signature_%d.svg",
                    System.currentTimeMillis()
                )
            )
            val stream: OutputStream = FileOutputStream(svgFile)
            val writer = OutputStreamWriter(stream)
            writer.write(signatureSvg)
            writer.close()
            stream.flush()
            stream.close()
            scanMediaFile(svgFile)
            result = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }

    companion object {
        const val ARGS_ROLE = "ROLE_ID"
        const val ARGS_SHARED = "SHARED_PREF"
        const val EXTRA_DATA = "extra_data"
        private const val REQUEST_EXTERNAL_STORAGE = 1
        private val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        fun verifyStoragePermissions(activity: DetailAmplopActivity?) {
            val permission = ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                )
            }
        }
    }

}