package com.aplikasijagad.kurir

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aplikasijagad.*
import com.aplikasijagad.API.Repository
import com.aplikasijagad.API.ResiListener
import com.aplikasijagad.Model.DataAPI
import com.aplikasijagad.R
import com.aplikasijagad.ViewModel.MainViewModel
import com.aplikasijagad.ViewModel.MainViewModelFactory
import com.aplikasijagad.adapter.ResiAdapter
import com.aplikasijagad.adapter.SuratJalanAdapter
import com.aplikasijagad.database.Order
import com.aplikasijagad.models.Users
import com.aplikasijagad.databinding.FragmentHomeKurirBinding
import com.aplikasijagad.models.SURATJALAN
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home_kurir.tv_totkurir
import kotlinx.android.synthetic.main.list_amplop.view.*
import kotlinx.android.synthetic.main.list_dashboard.*
import kotlinx.android.synthetic.main.list_laporan_kurir.view.*
import kotlinx.android.synthetic.main.list_suratjalan.view.*

class HomeKurirFragment : Fragment(), ResiListener {

    private lateinit var viewModel: MainViewModel
    private val myAdapter by lazy {
        ResiAdapter()
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var listUsers: MutableList<Users>
    private lateinit var listSuratjalan: MutableList<SURATJALAN>
    private lateinit var adapter: AdapterUtil<SURATJALAN>
    private lateinit var user: FirebaseUser
    private lateinit var binding: FragmentHomeKurirBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        listUsers = mutableListOf()
        listSuratjalan = mutableListOf()
        user = auth.currentUser!!
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home_kurir, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //reclerview show
        viewModel.getResiviewModel()
        viewModel.myResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response.isSuccessful) {
                response.body()?.let {
                    myAdapter.setData(it)

                }
            } else {
            }


        })

        setupRecylerview()
        infoProfile()
        //orderKurir()

    }

    private fun setupRecylerview() {
        val recylerView = binding.rvLaporanKurir
        recylerView.adapter = myAdapter
        recylerView.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun infoProfile() {
        val uid = user.uid

        database.getReference("Users").orderByChild("uid").equalTo(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(
                        requireContext(),
                        "Error",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (userSnapshot in p0.children) {
                            val data = userSnapshot.getValue(Users::class.java)
                            data?.let { listUsers.add(it) }
                            tv_totkurir.text = data!!.name
                        }
                    }
                }
            })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeKurirFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun onResiClicked(view: View , dataAPI: DataAPI) {
        Toast.makeText(context, "dataAPI.hp_driver", Toast.LENGTH_SHORT).show()
    }
}