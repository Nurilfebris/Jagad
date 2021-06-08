package com.aplikasijagad.Kategori

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.aplikasijagad.R
import com.aplikasijagad.kurir.ProfileKurirFragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_dashboard_kurir.*

class KategoriActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kategori)

        addFragment(KategoriFragment.newInstance())
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
                    replaceFragment(KategoriFragment.newInstance())
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