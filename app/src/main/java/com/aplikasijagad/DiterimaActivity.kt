package com.aplikasijagad

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.aplikasijagad.databinding.ActivityDiterimaBinding
import com.aplikasijagad.models.Amplop
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnSuccessListener
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
import java.text.SimpleDateFormat
import java.util.*

class DiterimaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiterimaBinding
    private lateinit var submit: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var listAmplop: MutableList<Amplop>

    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null
    private lateinit var database: FirebaseDatabase
    private var storageRef: StorageReference? = null
    private lateinit var user: FirebaseUser

    private lateinit var textInputLayout: TextInputLayout
    private lateinit var dropDownText: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diterima)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        listAmplop = mutableListOf()
        user = auth.currentUser!!

        storageRef = FirebaseStorage.getInstance().reference.child("Bukti images")

        dropDown()
        val penerima = findViewById<EditText>(R.id.penerima).text

        val data = intent.getParcelableExtra<Amplop>("data")

        val idSRJ = data?.idSRJ.toString()
        val idAMP = data?.noamplop.toString()
        val amplop = database.getReference("SURATJALAN").child(idSRJ).child("Amplop")
            .child(idAMP)

//        onItemSelectedStatus()
        capture_btn.setOnClickListener {
            //if system os is Marshmallow or Above, we need to request runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                    //permission was not enabled
                    val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    //show popup to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    openCamera()
                }
            }
            else{
                //system os is < marshmallow
                openCamera()
            }
        }

        save_data.setOnClickListener {
            amplop.child("diterima").setValue(penerima.toString())
//            amplop.child("pilihpenerima").setValue(spinStatus.toString())
            amplop.child("imageUrl").setValue(image_uri)

            amplop.child("status").setValue("Diterima")

            btn_terima.visibility = View.INVISIBLE
            btn_tolak.visibility = View.INVISIBLE
        }
    }

    private fun dropDown() {
        textInputLayout = findViewById(R.id.spinStatus)
        dropDownText = findViewById(R.id.dropdown_text)

        val items = arrayOf(
            "Satpam" ,
            "Teman Kantor" ,
            "Temen Kerja" ,
            "Temen Dekat" ,
            "Sahabat Selamanya" ,
            "Orang Rumah"
        )

        val adapter = ArrayAdapter(
            this ,
            R.layout.dropddown_item ,
            items
        )

        dropDownText.setAdapter(adapter)
        dropDownText.onItemClickListener =
            AdapterView.OnItemClickListener { p0 , p1 , p2 , p3 ->
                val category = items[p2]
                dropDownText.setText(category)
            }
    }

//    private fun onItemSelectedStatus() {
//        val option_penerima = binding.spinStatus
//        val options_penerima = arrayOf("Satpam", "Teman Kerja", "Orang Rumah", "Mandor")
//        option_penerima.adapter =
//            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options_penerima)
//        option_penerima.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onItemSelected(
//                parent: AdapterView<*>? ,
//                view: View? ,
//                position: Int ,
//                id: Long
//            ) {
//            }
//        }
//
//    }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //called when image was captured from camera intent
        if (resultCode == Activity.RESULT_OK){
            //set image captured to image view
            image_view.setImageURI(image_uri)
            uploadImageToDatabase()
        }
    }


    private fun uploadImageToDatabase(){

        if (image_uri!=null){

            val fileRef = storageRef!!.child(System.currentTimeMillis().toString()+".jpg")
            var uploadTask: StorageTask<*>
            uploadTask = fileRef.putFile(image_uri!!)

            uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{ task ->

                if (!task.isSuccessful)
                {
                    task.exception?.let{
                        throw it
                    }
                }
                return@Continuation fileRef.downloadUrl
            }).addOnCompleteListener{ task ->
                if (task.isSuccessful){

                    val downloadUrl = task.result
                    val url = downloadUrl.toString()

                    val mapProfileImage = HashMap<String,Any>()
                    mapProfileImage["profile"] = url
                }
            }
        }
    }
}