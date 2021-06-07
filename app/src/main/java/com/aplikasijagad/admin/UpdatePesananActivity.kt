package com.aplikasijagad.admin

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aplikasijagad.AdapterUtil
import com.aplikasijagad.DetailOrderActivity
import com.aplikasijagad.R
import com.aplikasijagad.adapter.CariAdapter
import com.aplikasijagad.databinding.ActivityUpdatePesananBinding
import com.aplikasijagad.models.SURATJALAN
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_update_pesanan.*
import kotlinx.android.synthetic.main.list_suratjalan.view.*


class UpdatePesananActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdatePesananBinding
    lateinit var ref: DatabaseReference
    lateinit var amplopList: MutableList<SURATJALAN>
    private lateinit var adapter: AdapterUtil<SURATJALAN>
    lateinit var searchtext:EditText
    lateinit var recyclerView: RecyclerView
    private lateinit var database: FirebaseDatabase
    private var listData: ArrayList<SURATJALAN>? = null
      var databaseReference = FirebaseDatabase.getInstance().reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        amplopList = mutableListOf()
        database = FirebaseDatabase.getInstance()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_pesanan)

        //cari data
        country_search.setOnClickListener {
            if (country_search.isEmpty()){
                Toast.makeText(applicationContext, "Inputan tidak boleh kosong!!", Toast.LENGTH_SHORT)
            }else{
              //  cariAmplop()

            }
        }

        //showEnviroBoxListFirebase()
        recyclerView = findViewById(R.id.rvListAmplop)


        database.getReference("SURATJALAN").orderByChild("uiDriver").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (eventSnapshot in p0.children) {
                        val data = eventSnapshot.getValue(SURATJALAN::class.java)
                        data?.let {
                            amplopList.add(it)
                        }
                    }
                }

                adapter = AdapterUtil(R.layout.list_suratjalan, amplopList, { itemView, item ->
                    itemView.tv_noSTB.text = item.uidSRJ
                    itemView.tv_tglsurat.text = item.tanggal
                    itemView.tv_Tujuan.text = item.tujuan
                    itemView.tv_drive.text = item.driver
                }, { _, item ->
                    val intent = Intent(this@UpdatePesananActivity, DetailOrderActivity::class.java)
                    intent.putExtra("data", item)
                    startActivity(intent)
                })

                rvListAmplop.apply {
                    adapter = this@UpdatePesananActivity.adapter
                    layoutManager = LinearLayoutManager(context)
                }
            }
        })
    }


//    private var mAdapter: CariAdapter? = null
//    private fun setupRecyclerview(data: ArrayList<SURATJALAN>) {
//
//        //set data recylerview
//        mAdapter = CariAdapter(data)
//        rvListAmplop.setAdapter(mAdapter)
//        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
//        rvListAmplop.setLayoutManager(layoutManager)
//        rvListAmplop.setItemAnimator(DefaultItemAnimator())
//
//    }
//    private fun sortedArray(
//        data: ArrayList<SURATJALAN> ,
//        query: String
//    ): ArrayList<SURATJALAN>? {
//        val tempData: ArrayList<SURATJALAN> = ArrayList()
//        for (i in 0 until data.size) {
//            if (data[i].driver.toLowerCase().contains(query)) {
//                tempData.add(data[i])
//            }
//        }
//        return tempData
//    }
//    private fun showEnviroBoxListFirebase() {
//        databaseReference.child("SURATJALAN").addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                listData = ArrayList()
//                for (item in snapshot.children) {
//                    val suratjalan: SURATJALAN = item.getValue(SURATJALAN::class.java)!!
//                    suratjalan.uidSRJ= item.toString()
//                    listData!!.add(suratjalan)
//                }
//                setupRecyclerview(listData!!)
//            }
//
//            override fun onCancelled(error: DatabaseError) {}
//        })
//    }

//    private fun cariAmplop() {
//        val searchView: SearchView = country_search as SearchView
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(s: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(s: String?): Boolean {
//                setupRecyclerview(sortedArray(listData!! , s!!)!!)
//                return true
//            }
//        })
//        searchView.setQueryHint("Search")
//    }

}