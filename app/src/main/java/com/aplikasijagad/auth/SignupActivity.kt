package com.aplikasijagad.auth

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.aplikasijagad.R
import com.aplikasijagad.admin.DashboardAdmin
import com.aplikasijagad.kurir.DashboardKurir
import com.aplikasijagad.models.Users
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*
import kotlin.math.*

@Suppress("DEPRECATION", "PrivatePropertyName")
class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var usertype: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val REQUEST_LOCATION_PERMISSION = 1
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var latitudeT: Double = -6.9527386
    private var longitudeT: Double = 107.6651714
    private var resultInMeter = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")
        usertype = ""
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        btn_admin.setOnClickListener {
            usertype = "Spv"
            btn_admin.setBackgroundColor(resources.getColor(R.color.gray))
            btn_courier.setBackgroundColor(resources.getColor(R.color.white))
        }

        btn_courier.setOnClickListener {
            getLastLocation()
            usertype = "Driver"
            btn_courier.setBackgroundColor(resources.getColor(R.color.gray))
            btn_admin.setBackgroundColor(resources.getColor(R.color.white))
        }

        btn_signup.setOnClickListener {
            signUp()
        }

        tv_loginacc.setOnClickListener {
            logIn()
        }
    }

    private fun logIn() {
        startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
    }

    private fun signUp() {
        if (checkInput()) {
            if (usertype == "Spv" || usertype == "Driver") {
                val name = et2_name.text.toString().trim()
                val nik = et2_nik.text.toString().trim()
                val email = et2_email.text.toString().trim()
                val password = et2_password.text.toString().trim()
                val phone = et2_phone.text.toString().trim()
                val address = et2_address.text.toString().trim()

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful) {
                            val uid = auth.currentUser!!.uid
                            val data = Users(
                                uid,
                                name,
                                nik,
                                email,
                                password,
                                phone,
                                address
//                                usertype,
//                                latitude,
//                                longitude
                            )
                            database.child(uid).setValue(data)
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        this@SignupActivity,
                                        e.toString(),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this@SignupActivity,
                                        "Data has been saved",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    if (usertype == "Spv") {
                                        startActivity(
                                            Intent(
                                                this@SignupActivity,
                                                DashboardAdmin::class.java
                                            )
                                        )
                                    } else if (usertype == "Driver") {
                                        startActivity(
                                            Intent(
                                                this@SignupActivity,
                                                DashboardKurir::class.java
                                            )
                                        )
                                    }
                                }
                        }
                    }
            } else {
                Toast.makeText(
                    this@SignupActivity,
                    "Select User",
                    Toast.LENGTH_LONG
                ).show()

            }
        }
    }

    private fun checkInput(): Boolean {
        return if (et2_name.text.isNullOrBlank() || et2_nik.text.isNullOrBlank() || et2_email.text.isNullOrBlank() || et2_password.text.isNullOrBlank() || et2_phone.text.isNullOrBlank() || et2_address.text.isNullOrBlank()) {
            Toast.makeText(this@SignupActivity, "Fields cannot be null", Toast.LENGTH_SHORT).show()
            false
        } else
            true
    }

    fun getLastLocation(){
        if(CheckPermission()){
            if(isLocationEnabled()){
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // request permission
                    ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION);
                    return
                }
                fusedLocationClient.lastLocation.addOnCompleteListener { task->
                    var location:Location? = task.result
                    if(location == null){
                        NewLocationData()
                    }else{
                        NewLocationData()
                        latitude = location.latitude
                        longitude = location.longitude
                        var lon1 = Math.toRadians(longitudeT)
                        var lon2 = Math.toRadians(longitude)
                        var lat1 = Math.toRadians(latitudeT)
                        var lat2 = Math.toRadians(latitude)

                        var dlon = lon2 - lon1
                        var dlat = lat2 - lat1

                        var a = Math.pow(Math.sin(dlat / 2), 2.0)+ Math.cos(lat1) * Math.cos(lat2)* Math.pow(Math.sin(dlon / 2), 2.0)
                        var c = 2 * Math.asin(Math.sqrt(a))
                        var r = 6371
                        var result = c*r
                        resultInMeter = result*1000

                        if (resultInMeter>= 100){
                            Log.d("statusJarak","Gagal")
                        }else
                            Log.d("statusJarak","Berhasil")

                        Log.d("userlocation", "Latitude: "+latitude+" Longtitude: "+longitude)
                        Log.d("Distance","jarak = "+resultInMeter+" M")
                    }
                }
            }else{
                Toast.makeText(this,"Please Turn on Your device Location",Toast.LENGTH_SHORT).show()
            }
        }else{
            RequestPermission()
        }
    }

    fun NewLocationData(){
        var locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // request permission
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION);
            return
        }
        fusedLocationClient!!.requestLocationUpdates(
            locationRequest,locationCallback, Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation
            Log.d("Debug:","your last last location: "+ lastLocation.longitude.toString())
        }
    }

    private fun CheckPermission():Boolean{
        if(
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }

        return false
    }

    fun isLocationEnabled():Boolean{
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun RequestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_LOCATION_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //called when user presses ALLOW or DENY from Permission Request Popup
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.size > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }
}