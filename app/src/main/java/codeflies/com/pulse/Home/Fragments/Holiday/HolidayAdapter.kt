package com.codeflies.supertravel.TabsLayou.TabLayoutFragment.UpComingRides

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import codeflies.com.pulse.Helpers.FunctionClass
import codeflies.com.pulse.Models.Candidates.CandidatesItem
import codeflies.com.pulse.Models.Holidays.HolidaysItem
import codeflies.com.pulse.Models.Leaves.LeavesItem
import codeflies.com.pulse.R
import codeflies.com.pulse.databinding.CandidateItemListBinding
import codeflies.com.pulse.databinding.HolidayItemListBinding
import codeflies.com.pulse.databinding.LeaveItemListBinding
import java.text.SimpleDateFormat
import java.util.*


class HolidayAdapter(
    val context: Context, private var list: List<HolidaysItem>?
) :
    RecyclerView.Adapter<HolidayAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = HolidayItemListBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.title.text = list?.get(position)?.title
        if(list?.get(position)?.durationDays!! >1){
            holder.binding.dates.text =
                FunctionClass.changeDate(list?.get(position)?.startAt) + " - " + FunctionClass.changeDate(
                    list?.get(position)?.endAt
                )
        }else{
            holder.binding.dates.text =
                FunctionClass.changeDate(list?.get(position)?.startAt)
        }

        holder.binding.duration.text = list?.get(position)?.durationDays.toString() + " Days"
        holder.binding.type.text = list?.get(position)?.dayType?.capitalize()


    }


    override fun getItemCount(): Int {
        return list!!.size
    }


    inner class MyViewHolder(val binding: HolidayItemListBinding) :
        RecyclerView.ViewHolder(binding.root)

}