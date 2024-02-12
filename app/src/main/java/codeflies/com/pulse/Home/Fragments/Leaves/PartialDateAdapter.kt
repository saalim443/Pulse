package com.codeflies.supertravel.TabsLayou.TabLayoutFragment.UpComingRides

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import codeflies.com.pulse.Helpers.Constants
import codeflies.com.pulse.Helpers.FunctionClass
import codeflies.com.pulse.Home.Fragments.Leaves.LeaveDetailsActivity
import codeflies.com.pulse.Models.Leaves.LeavesItem
import codeflies.com.pulse.Models.Leaves.PartialLeaveDate
import codeflies.com.pulse.R
import codeflies.com.pulse.databinding.LeaveItemListBinding
import codeflies.com.pulse.databinding.PartialDateItemListBinding

import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*


class PartialDateAdapter(
    val context: Context, private var list: List<PartialLeaveDate>?
) :
    RecyclerView.Adapter<PartialDateAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = PartialDateItemListBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.date.text = list?.get(position)?.date
        holder.binding.cDate.text = list?.get(position)?.cDate
        LeaveDetailsActivity.finalList.add(PartialLeaveDate(list?.get(position)?.date,"Pending","1"))
        val adapter = ArrayAdapter<String>(
            context,
            android.R.layout.simple_spinner_item,
            arrayOf("Pending","Approved","Rejected")
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.binding.status.adapter = adapter

        holder.binding.status.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                   var selectedStatus =parent?.selectedItem.toString()
                    LeaveDetailsActivity.saveValue(list?.get(position)?.date.toString(),selectedStatus,position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }
            }
    }




    override fun getItemCount(): Int {
        return list!!.size
    }



    inner class MyViewHolder(val binding: PartialDateItemListBinding) : RecyclerView.ViewHolder(binding.root)

}