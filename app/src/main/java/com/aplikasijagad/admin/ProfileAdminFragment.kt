package com.aplikasijagad.admin

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.aplikasijagad.MainActivity
import com.aplikasijagad.R
import com.aplikasijagad.models.Users
import com.aplikasijagad.databinding.FragmentProfileAdminBinding
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile_admin.*

class ProfileAdminFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    var database: DatabaseReference? = null
    private lateinit var listUsers: MutableList<Users>
    private lateinit var binding: FragmentProfileAdminBinding
    private val RequestCode = 438
    private var imageUri: Uri?  = null
    private var storageRef: StorageReference? = null

    var usersReference : DatabaseReference? = null
    var firebaseUser : FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")

        firebaseUser = FirebaseAuth.getInstance().currentUser
        usersReference = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)

        storageRef = FirebaseStorage.getInstance().reference.child("Admin images")
        listUsers = mutableListOf()

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile_admin, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        load()

//        profile_image_setting.setOnClickListener {
//            pickImage()
//        }

        logoutAdmin.setOnClickListener {
            auth.signOut()
            startActivity(Intent(requireActivity(), MainActivity::class.java))
        }
    }

    private fun load() {
        val uid = auth.currentUser!!.uid

        database!!.orderByChild("uid").equalTo(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(
                        requireContext(),
                        "Error",
                        Toast.LENGTH_LONG
                    )
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (userSnapshot in p0.children) {
                            val data = userSnapshot.getValue(Users::class.java)
                            data?.let { listUsers.add(it) }
                            tv_namaAdmin.text = data!!.name
                            tv_nikAdmin.text = data.nik
                            tv_nohpAdmin.text = data.phone
                            tv_emailAdmin.text = data.email
                            tv_Admin.text = data.usertype
                            tv_alamatAdmin.text = data.address
                        }
                    }
                }
            })
    }

//    private fun pickImage() {
//        val intent = Intent()
//        intent.type = "image/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//        startActivityForResult(intent,RequestCode)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == RequestCode && resultCode == Activity.RESULT_OK && data!!.data!=null){
//            imageUri = data.data
//            Toast.makeText(context,"Uploading  Processing",Toast.LENGTH_LONG).show()
//            uploadImageToDatabase()
//        }
//    }
//
//    private fun uploadImageToDatabase(){
//        val progressBar = ProgressDialog(context)
//        progressBar.setMessage("Image is uploading, please wait...")
//        progressBar.show()
//
//        if (imageUri!=null){
//
//            val fileRef = storageRef!!.child(System.currentTimeMillis().toString()+".jpg")
//            var uploadTask: StorageTask<*>
//            uploadTask = fileRef.putFile(imageUri!!)
//
//            uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{ task ->
//
//                if (!task.isSuccessful)
//                {
//                    task.exception?.let{
//                        throw it
//                    }
//                }
//                return@Continuation fileRef.downloadUrl
//            }).addOnCompleteListener{ task ->
//                if (task.isSuccessful){
//
//                    val downloadUrl = task.result
//                    val url = downloadUrl.toString()
//                    val uid = auth.currentUser!!.uid
//
//                    val mapProfileImage = HashMap<String,Any>()
//                    mapProfileImage["profile"] = url
//                    usersReference!!.updateChildren(mapProfileImage)
//                }
//                progressBar.dismiss()
//            }
//        }
//    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileAdminFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}