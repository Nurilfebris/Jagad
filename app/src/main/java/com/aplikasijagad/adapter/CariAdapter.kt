package com.aplikasijagad.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.aplikasijagad.R
import com.aplikasijagad.databinding.ListSuratjalanBinding
import com.aplikasijagad.models.Amplop
import com.aplikasijagad.models.SURATJALAN
import com.itextpdf.text.ListItem

class CariAdapter (private val cari: List<SURATJALAN>) : RecyclerView.Adapter<CariAdapter.CariViewHolder>(){
    //var listener: AdapterListener<DummyParent>? = null
    inner class CariViewHolder(val listitemcaribinding:ListSuratjalanBinding)
        :RecyclerView.ViewHolder(listitemcaribinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CariViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_suratjalan,
            parent,
            false
        )
    )

    override fun getItemCount() = cari.size


    override fun onBindViewHolder(holder: CariViewHolder , position: Int) {

        holder.listitemcaribinding.tvDrive.text=cari[position].driver
    }
}