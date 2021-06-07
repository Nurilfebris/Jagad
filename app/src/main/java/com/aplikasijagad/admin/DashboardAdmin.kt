package com.aplikasijagad.admin

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.aplikasijagad.API.LocationService
import com.aplikasijagad.API.ServiceBuilder
import com.aplikasijagad.R
import com.aplikasijagad.models.Location
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import kotlinx.android.synthetic.main.activity_dashboard_admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardAdmin : AppCompatActivity() {
    @SuppressLint("PrivateResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_admin)


        addFragment(HomeAdminFragment.newInstance())
        bottomNavigation.show(0)
        bottomNavigation.add(MeowBottomNavigation.Model(0,R.drawable.homeicon))
        bottomNavigation.add(MeowBottomNavigation.Model(1,R.drawable.akun))

        bottomNavigation.setOnClickMenuListener {
            when(it.id){
                0 -> {
                    replaceFragment(HomeAdminFragment.newInstance())
                }
                1 -> {
                    replaceFragment(ProfileAdminFragment.newInstance())
                }
                else -> {
                    replaceFragment(HomeAdminFragment.newInstance())
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

}