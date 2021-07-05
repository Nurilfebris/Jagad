package com.aplikasijagad

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.aplikasijagad.API.Repository
import com.aplikasijagad.Model.DataAPI
import com.aplikasijagad.ViewModel.MainViewModel
import com.aplikasijagad.ViewModel.MainViewModelFactory
import com.aplikasijagad.kurir.DashboardKurir
import com.aplikasijagad.models.Amplop
import com.github.gcacace.signaturepad.views.SignaturePad
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_detail_amplop.*
import kotlinx.android.synthetic.main.activity_diterima.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.util.*

class DiterimaActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var listAmplop: MutableList<Amplop>
    private lateinit var viewModel: MainViewModel
    private lateinit var categori: String

    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    private var image_uri: Uri? = null
    private var postPath: String? = null
    private lateinit var database: FirebaseDatabase
    private var storageRef: StorageReference? = null
    private lateinit var user: FirebaseUser

    private lateinit var textInputLayout: TextInputLayout
    private lateinit var dropDownText: AutoCompleteTextView
    lateinit var preferences: SharedPreferences
    lateinit var preferences2: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diterima)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        listAmplop = mutableListOf()
        user = auth.currentUser!!
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        storageRef = FirebaseStorage.getInstance().reference.child("Diterima Sukses")

        //save signature
        btnSave.setOnClickListener {
            val signatureBitmap = signaturePad.getSignatureBitmap()
            if (addJpgSignatureToGallery(signatureBitmap)) {
                Toast.makeText(
                    this, "Tanda tangan disimpan ke dalam Galeri",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("ttd", addJpgSignatureToGallery(signatureBitmap).toString())

            } else {
                Toast.makeText(
                    this, "Tidak dapat menyimpan Tanda Tangan",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (addSvgSignatureToGallery(signaturePad.getSignatureSvg())) {
                Toast.makeText(
                    this, "Tanda tangan SVG disimpan ke dalam Galeri",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("ttdyy", addSvgSignatureToGallery(signaturePad.signatureSvg).toString())
            } else {
                Toast.makeText(
                    this, "Tidak dapat menyimpan Tanda Tangan SVG",
                    Toast.LENGTH_SHORT
                ).show()
            }

            preferences = this.getSharedPreferences(ARGS_ROLE, Context.MODE_PRIVATE)
            preferences2 = this.getSharedPreferences(ARGS_ROLE2, Context.MODE_PRIVATE)
            val roleId = preferences.getString(ARGS_ROLE, "id_amplop")
            val namadriver = preferences2.getString(ARGS_ROLE2, "nama_driver")
            Log.d("yy", roleId.toString())
            Log.d("yy", namadriver.toString())
            val extra = intent.extras

            Log.d("Hahah", "test berhasil")

            Log.d("cobates", "ssssssss")
            Log.d("cobates", roleId.toString())
            Log.d("cobates", penerimayee.text.toString())
            Log.d("cobates", dropDownText.text.toString())
            Log.d("cobates", image_uri?.path.toString())

            val mobile_driver_diterima_nama = penerimayee.text.toString()
            val mobile_driver_diterima_jenis_penerima = dropDownText.text.toString()

            updateSukses(
                id_amplop = roleId.toString(),
                mobile_driver_diterima_jenis_penerima = mobile_driver_diterima_jenis_penerima,
                mobile_driver_pengantar = namadriver.toString(),
                mobile_driver_diterima_nama = mobile_driver_diterima_nama
            )
            Log.d("cobadata", roleId.toString())
            Toast.makeText(
                this, "Berhasil",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(this, DashboardKurir::class.java))
        }

        dropDown()

        //set permission
        verifyStoragePermissions(this);

        //draw signature
        signaturePad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {
                //Toast.makeText(this@MainActivity, "Mulai menulis...", Toast.LENGTH_SHORT).show()
            }

            override fun onSigned() {
                btnSave.setEnabled(true)
                btnClear.setEnabled(true)

            }

            override fun onClear() {
                btnSave.setEnabled(false)
                btnClear.setEnabled(false)

            }
        })

        //clear signature
        btnClear.setOnClickListener {
            signaturePad.clear()
        }

        capture_btn.setOnClickListener {
            //if system os is Marshmallow or Above, we need to request runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED
                ) {
                    //permission was not enabled
                    val permission = arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    //show popup to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                } else {
                    //permission already granted
                    openCamera()
                }
            } else {
                //system os is < marshmallow
                openCamera()
            }
        }
    }

    override fun onSaveInstanceState(oldInstanceState: Bundle) {
        super.onSaveInstanceState(oldInstanceState)
        oldInstanceState.clear()
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
                // val category = items[p2]
                categori = items[p2]
                dropDownText.setText(categori)
            }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
        Log.d("opencamera", "muncul")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //called when image was captured from camera intent
        if (resultCode == Activity.RESULT_OK) {
            //set image captured to image view
            image_view.setImageURI(image_uri)
            postPath = image_uri!!.path

            Log.d("bismmilaah", image_uri!!.path.toString())

            uploadImageToDatabase()
        }
    }

    private fun uploadImageToDatabase() {
        if (image_uri != null) {

            preferences = this.getSharedPreferences(ARGS_ROLE, Context.MODE_PRIVATE)
            val roleId = preferences.getString(ARGS_ROLE, "id_amplop")

            val fileRef = storageRef!!.child(roleId.toString()  + ".jpg")
            var uploadTask: StorageTask<*>
            uploadTask = fileRef.putFile(image_uri!!)
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->

                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation fileRef.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val downloadUrl = task.result
                    val url = downloadUrl.toString()

                    val mapProfileImage = HashMap<String, Any>()
                    mapProfileImage["profile"] = url
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.size <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this, "Tidak dapat menulis gambar ke Media Penyimpanan",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        if (requestCode == PERMISSION_CODE) {
            if (grantResults.size > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                openCamera()
            }
        }
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

    //Image Signature
    //@Throws(IOException::class)
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
        const val ARGS_ROLE2 = "ROLE_ID2"
        const val EXTRA_DATA = "extra_data"
        private const val REQUEST_EXTERNAL_STORAGE = 1
        private val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        fun verifyStoragePermissions(activity: DiterimaActivity?) {
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

    private fun updateSukses(
        id_amplop: String,
        mobile_driver_diterima_jenis_penerima: String,
        mobile_driver_pengantar: String,
        mobile_driver_diterima_nama: String
    ) {
        viewModel.putUpdateBerhasilViewModel(
            id_amplop,
            mobile_driver_diterima_jenis_penerima,
            mobile_driver_pengantar,
            mobile_driver_diterima_nama
        )
        viewModel.myResponseUpdateBerhasil.observe(this, { response ->
            when {
                response.isSuccessful -> {
                    Log.d("hahasukses", response.body().toString())
                    Log.d("hahasukses", response.code().toString())
                }
            }
        })
    }

}