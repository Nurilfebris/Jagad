package com.aplikasijagad.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.aplikasijagad.admin.DashboardAdmin
import com.aplikasijagad.kurir.DashboardKurir
import com.aplikasijagad.R
import com.aplikasijagad.models.Users
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var usertype: String
    private lateinit var listUsers: MutableList<Users>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")
        listUsers = mutableListOf()
        usertype = ""

//        btn_logadmin.setOnClickListener {
//            usertype = "Spv"
//            btn_logadmin.setBackgroundColor(resources.getColor(R.color.gray))
//            btn_logcourier.setBackgroundColor(resources.getColor(R.color.white))
//        }
//
//        btn_logcourier.setOnClickListener {
//            usertype = "Driver"
//            btn_logcourier.setBackgroundColor(resources.getColor(R.color.gray))
//            btn_logadmin.setBackgroundColor(resources.getColor(R.color.white))
//        }

        btn_login.setOnClickListener {
            logIn()
        }
    }

    private fun logIn() {
        if (checkInput()) {
            val phone = et2_logphone.text.toString().trim()
            val password = et2_logpassword.text.toString().trim()

            database.orderByChild("phone").equalTo(phone)
                .addValueEventListener(object : ValueEventListener {

                    override fun onCancelled(p0: DatabaseError) {
                        Toast.makeText(
                            this@LoginActivity ,
                            "Database Error" ,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onDataChange(p0: DataSnapshot) {  
                        if (p0.exists()) {
                            for (userSnapshot in p0.children) {
                                val pass = userSnapshot.getValue(Users::class.java)
                                pass?.let {
                                    listUsers.add(it)
                                    usertype = pass.usertype
                                    Log.d("pirapira", usertype)
                                }
                                if (pass!!.password == password) {
                                    loginUser(pass.email , password)
                                } else {
                                    Toast.makeText(
                                        this@LoginActivity ,
                                        "Wrong Password" ,
                                        Toast.LENGTH_LONG
                                    ).show()

                                }
                            }
                        } else {
                            Toast.makeText(
                                this@LoginActivity ,
                                "NIK Not Found" ,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                })
        }
    }

    private fun checkInput(): Boolean {
        return if (et2_logphone.text.isNullOrBlank() || et2_logpassword.text.isNullOrBlank()) {
            Toast.makeText(this@LoginActivity , "Fields cannot be null" , Toast.LENGTH_SHORT).show()
            false
        } else
            true
    }

    private fun loginUser(email: String , password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email , password)
            .addOnCompleteListener { task ->
                checkResult(task)
            }
    }

    private fun checkResult(task: Task<AuthResult>) {
        if (task.isSuccessful) {
            if (auth.currentUser != null) {
                if (usertype == "Spv") {
                    startActivity(
                        Intent(
                            this@LoginActivity ,
                            DashboardAdmin::class.java
                        )
                    )
                } else if (usertype == "Driver") {
                    startActivity(
                        Intent(
                            this@LoginActivity ,
                            DashboardKurir::class.java
                        )
                    )
                } else {
                    Toast.makeText(
                        this@LoginActivity ,
                        "No User Detected" ,
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(
                    this@LoginActivity ,
                    "No User Detected" ,
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Toast.makeText(this , "Error: ${task.exception?.message}" , Toast.LENGTH_LONG)
                .show()
        }
    }
}
