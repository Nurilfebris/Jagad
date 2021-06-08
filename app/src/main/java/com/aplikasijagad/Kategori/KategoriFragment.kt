package com.aplikasijagad.Kategori

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aplikasijagad.API.Repository
import com.aplikasijagad.API.ResiListener
import com.aplikasijagad.AdapterUtil
import com.aplikasijagad.Model.DataAPI
import com.aplikasijagad.R
import com.aplikasijagad.ViewModel.MainViewModel
import com.aplikasijagad.ViewModel.MainViewModelFactory
import com.aplikasijagad.adapter.ResiAdapter
import com.aplikasijagad.databinding.FragmentHomeKurirBinding
import com.aplikasijagad.databinding.FragmentKategoriBinding
import com.aplikasijagad.kurir.DashboardKurir
import com.aplikasijagad.models.SURATJALAN
import com.aplikasijagad.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home_kurir.*
import kotlinx.android.synthetic.main.fragment_kategori.*
import kotlinx.android.synthetic.main.list_dashboard.*

class KategoriFragment : Fragment(), ResiListener, View.OnClickListener {
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
    private lateinit var binding: FragmentKategoriBinding
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
            DataBindingUtil.inflate(inflater, R.layout.fragment_kategori, container, false)
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

        infoProfile()
        //orderKurir()
        cv_toko.setOnClickListener {
            val intent = Intent (getActivity(), DashboardKurir::class.java)
            getActivity()?.startActivity(intent)
            getActivity()?.finish()
        }
        cv_pool.setOnClickListener {
            val intent = Intent (getActivity(), DashboardKurir::class.java)
            getActivity()?.startActivity(intent)
            getActivity()?.finish()
        }
        cv_ekspedisi.setOnClickListener {
            val intent = Intent (getActivity(), DashboardKurir::class.java)
            getActivity()?.startActivity(intent)
            getActivity()?.finish()
        }


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
                            tv_namadriver.text=data!!.name
                        }
                    }
                }
            })
    }
    companion object {
        @JvmStatic
        fun newInstance() =
            KategoriFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun onResiClicked(view: View , dataAPI: DataAPI) {
        Toast.makeText(context, "dataAPI.hp_driver", Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        Toast.makeText(context, "Its toast!", Toast.LENGTH_SHORT).show()
    }
}