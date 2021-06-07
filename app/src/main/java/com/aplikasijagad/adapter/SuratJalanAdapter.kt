package com.aplikasijagad.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aplikasijagad.AdapterUtil
import com.aplikasijagad.R
import kotlinx.android.synthetic.main.list_suratjalan.view.*

class SuratJalanAdapter<T>(var layout: Int,
                           private var items: List<T>,
                           var view:(View, T) -> Unit,
                           var handler:(Int, T) -> Unit): RecyclerView.Adapter<SuratJalanAdapter.ViewHolder<T>>() {

    var data = this.items
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder<T>(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(item: T, view: (View, T) -> Unit) {
            view(itemView, item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(this.layout, parent, false))
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        val item: T = this.data[position]
        holder.bind(item, view)
        holder.itemView.setOnClickListener { handler(position, item) }
    }
}