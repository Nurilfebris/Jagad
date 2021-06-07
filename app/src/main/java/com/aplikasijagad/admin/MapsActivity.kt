package com.aplikasijagad

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.aplikasijagad.API.DataRepository
import com.aplikasijagad.API.LocationService
import com.aplikasijagad.API.ServiceBuilder
import com.aplikasijagad.models.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_maps.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var newestCoord: LatLng
    private lateinit var database: DatabaseReference
    private lateinit var databasesuratjalan:DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val actionbar = supportActionBar

        actionbar!!.title = "Maps"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        database = FirebaseDatabase.getInstance().getReference("DRIVER")
        databasesuratjalan = FirebaseDatabase.getInstance().getReference("SURATJALAN")

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        getMapUpdate()

        refresh_btn.setOnClickListener {
            getMapUpdate()
        }

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

    }

    private fun getMapUpdate() {
        //val usertype =
        val rootReference = FirebaseDatabase.getInstance().reference
        val reference = rootReference.child("DRIVER").orderByChild("uidSRJ")
        //val referencesuratjalan = rootReference.child("SURATJALAN")
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val latitude = ds.child("lat").getValue(Double::class.java)
                    val longitude = ds.child("lng").getValue(Double::class.java)
                    val name = ds.child("name").getValue(String::class.java)
                    val idsuratjalan=ds.child("uidSRJ").getValue(String::class.java)
                    Log.d("getDataCoor", "latitude= " + latitude + " longitude: " + longitude)

                    val userCoord = LatLng(latitude!! , longitude!!)
                    newestCoord = userCoord
                    val overLaySize=100f
                    mMap.addMarker(MarkerOptions()
                        .position(userCoord)
                        .title(name).snippet(idsuratjalan)
                        .icon(bitmapDescriptorFromVector(this@MapsActivity, R.drawable.trackingtruck)))

//                    val androidmarker= GroundOverlayOptions()
//                        .image(BitmapDescriptorFactory.fromResource(R.drawable.truckmarker))
//                        .position(userCoord, overLaySize)
//
//                    mMap.addGroundOverlay(androidmarker)
                }
                val zoomLevel = 15f
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newestCoord, zoomLevel))
            }

            override fun onCancelled(error: DatabaseError) {

                Log.d("TAGerror", error.getMessage())
            }
        }
        reference.addListenerForSingleValueEvent(valueEventListener)
        //referencesuratjalan.addListenerForSingleValueEvent(valueEventListener)
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

//    private fun loadMarkersFromDB(){
//
//        database.addListenerForSingleValueEvent(object: ValueEventListener{
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if(dataSnapshot.exists()){
//                    for(spotLatLng:DataSnapshot in dataSnapshot.children){
//                        val latitude = spotLatLng.child("lat").getValue(Double::class.java)
//                        val longitude = spotLatLng.child("lng").getValue(Double::class.java)
//                        newestCoord = LatLng(latitude!!, longitude!!)
//                    }
//                }
//            }
//            override fun onCancelled(p0: DatabaseError) {
//            }
//        } )
//    }

}
