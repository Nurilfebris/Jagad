package com.aplikasijagad.kurir

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.aplikasijagad.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ViewDeliveryOrderFragment : Fragment() {

    lateinit var ref : DatabaseReference
    lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ref = FirebaseDatabase.getInstance().getReference("Loket")

        return inflater.inflate(R.layout.fragment_view_delivery_order, container, false)
    }
}