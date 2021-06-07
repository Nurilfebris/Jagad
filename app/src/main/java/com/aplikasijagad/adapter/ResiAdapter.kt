package com.aplikasijagad.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.aplikasijagad.API.ResiListener
import com.aplikasijagad.DetailAmplopActivity
import com.aplikasijagad.MainActivity2
import com.aplikasijagad.Model.APICoronaProv
import com.aplikasijagad.Model.ApiSPB
import com.aplikasijagad.Model.DataAPI

//import com.aplikasijagad.Model.ResiBarang
import com.aplikasijagad.R
import com.aplikasijagad.databinding.ListAmplopBinding
import com.aplikasijagad.models.SURATJALAN
import kotlinx.android.synthetic.main.activity_detail_order.*
import kotlinx.android.synthetic.main.list_amplop.view.*

class ResiAdapter :
    RecyclerView.Adapter<ResiAdapter.ResiAdapterViewHolder>() {

    inner class ResiAdapterViewHolder(
        val recyclerciewListItemResiBinding
        : ListAmplopBinding
    ) :
        RecyclerView.ViewHolder(recyclerciewListItemResiBinding.root)


    private var myList = emptyList<DataAPI>()
    private var onItemSelectedListener : ResiListener? = null

    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int) = ResiAdapterViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context) ,
            R.layout.list_amplop , parent , false
        )
    )

    class ViewHolder<T>(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(item: T, view: (View, T) -> Unit) {
            view(itemView, item)


        }
    }

    override fun getItemCount(): Int = myList.size
    override fun onBindViewHolder(holder: ResiAdapterViewHolder , position: Int) {
        holder.recyclerciewListItemResiBinding.tvRincian1.text = myList[position].id_amplop
        holder.recyclerciewListItemResiBinding.detailRincianPenerima.text = myList[position].nama_penerima
        holder.recyclerciewListItemResiBinding.detailRincianPengirim.text = myList[position].nama_pengirim
        holder.recyclerciewListItemResiBinding.detailRincianAlamat.text = myList[position].alamat_pengirim
        holder.recyclerciewListItemResiBinding.detailRincianJenis.text = myList[position].tanggal
//        holder.recyclerciewListItemResiBinding.detailRincianStatus.text = myList[position].status

        holder.itemView.setOnClickListener { v->
            val intent = Intent(v.context,DetailAmplopActivity::class.java)
            intent.putExtra(DetailAmplopActivity.EXTRA_DATA, myList[position])
            v.context.startActivity(intent)
        }
    }

    fun setData(newList: List<DataAPI>) {
        myList = newList
        notifyDataSetChanged()
    }

    fun setOnClickItemListener(onClickListener: ResiListener){
        this.onItemSelectedListener = onClickListener
    }

}