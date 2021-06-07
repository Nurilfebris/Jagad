package com.aplikasijagad

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.aplikasijagad.admin.DashboardAdmin
import com.aplikasijagad.auth.LoginActivity
import com.aplikasijagad.auth.SignupActivity
import com.aplikasijagad.models.Users
import com.aplikasijagad.kurir.DashboardKurir
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var listUsers: MutableList<Users>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")
        listUsers = mutableListOf()

        if (FirebaseAuth.getInstance().currentUser != null) {
            checkUser()
        } else {
            setContentView(R.layout.activity_main)
        }

//        btn_admin.setOnClickListener {
//            startActivity(Intent(this@MainActivity, SignupActivity::class.java))
//        }

        btn_kurir.setOnClickListener {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }

    }

    private fun checkUser() {
        val uid = auth.currentUser!!.uid

        database.orderByChild("uid").equalTo(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(
                        this@MainActivity,
                        "Error",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (userSnapshot in p0.children) {
                            val pass = userSnapshot.getValue(Users::class.java)
                            pass?.let { listUsers.add(it) }
                            if (pass!!.usertype == "Spv") {
                                startActivity(Intent(this@MainActivity, DashboardAdmin::class.java))
                            } else if (pass!!.usertype == "Driver") {
                                startActivity(Intent(this@MainActivity, DashboardKurir::class.java))
                            }
                        }
                    }
                }
            })
    }

    @SuppressLint("PrivateResource")
    private fun launchIntent(activity: Class<*>) {
        val intent = Intent(applicationContext, activity)
        startActivity(intent)
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
    }

    @SuppressLint("PrivateResource")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
    }
}