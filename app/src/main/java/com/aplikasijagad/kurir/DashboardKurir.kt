package com.aplikasijagad.kurir

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.aplikasijagad.API.RestApiService
import com.aplikasijagad.Kategori.KategoriFragment
import com.aplikasijagad.R
import com.aplikasijagad.models.Location
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.google.android.gms.location.*

import kotlinx.android.synthetic.main.activity_dashboard_kurir.bottomNavigation


@Suppress("DEPRECATION")
class DashboardKurir : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var lat: Double = 0.0
    private var long: Double = 0.0

    private val REQUEST_LOCATION_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_kurir)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocationUpdates()
        addFragment(HomeKurirFragment.newInstance())
        bottomNavigation.show(0)
        bottomNavigation.add(MeowBottomNavigation.Model(0,R.drawable.homeicon))
        //bottomNavigation.add(MeowBottomNavigation.Model(1,R.drawable.list))
        bottomNavigation.add(MeowBottomNavigation.Model(1,R.drawable.akun))

        bottomNavigation.setOnClickMenuListener {
            when(it.id){
                0 -> {
                    replaceFragment(KategoriFragment.newInstance())
                }
//                1 -> {
//                    replaceFragment(HistoryFragment.newInstance())
//                }
                1 -> {
                    replaceFragment(ProfileKurirFragment.newInstance())
                }
                else -> {
                    replaceFragment(HomeKurirFragment.newInstance())
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.fragmentContainer,fragment).addToBackStack(Fragment::class.java.simpleName).commit()
    }

    private fun addFragment(fragment: Fragment){
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.add(R.id.fragmentContainer,fragment).addToBackStack(Fragment::class.java.simpleName).commit()
    }

     fun getLocationUpdates()
    {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest()
        locationRequest.setInterval(5000)
        locationRequest.setFastestInterval(5000)
        locationRequest.smallestDisplacement = 170f // 170 m = 0.1 mile
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY //set according to your app function
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return

                if (locationResult.locations.isNotEmpty()) {
                    // get latest location
                    val location =
                        locationResult.lastLocation
                        Log.d("latitude", location.latitude.toString())
                        Log.d("longitude", location.longitude.toString())
                        lat = location.latitude
                        long =location.longitude
                        addSample()
                    // use your location object
                    // get latitude , longitude and other info from this
                }
            }
        }
    }
    fun addSample(){
        val apiService = RestApiService()
        val dummyLocation = Location(
            lat = lat,
            long = long,
            keterangan = "Test Input Baru",
            driver_id = 5,
            timestemp = null
        )
        apiService.addLocation(dummyLocation){
            if (it?.driver_id == null){
                Log.d("hasilMsg","Error adding new location")
            }
            else{
                Log.d("berhasilMsg", "Berhasil")
            }
        }
    }
    //start location updates
    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this ,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this ,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null /* Looper */
        )
    }

    // stop location updates
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    // stop receiving location update when activity not visible/foreground
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    // start receiving location update when activity  visible/foreground
    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    
}