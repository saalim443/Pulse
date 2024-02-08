package com.codeflies.supertravel.TabsLayou.TabLayoutFragment.UpComingRides

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import codeflies.com.pulse.Helpers.Constants
import codeflies.com.pulse.Helpers.FunctionClass
import codeflies.com.pulse.Home.Fragments.Leaves.LeaveDetailsActivity
import codeflies.com.pulse.Models.Leaves.LeavesItem
import codeflies.com.pulse.R
import codeflies.com.pulse.databinding.LeaveItemListBinding
import codeflies.com.pulse.leaveDetaill.CandidateDetailActivity
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*


class LeaveAdapter(
    val context: Context, private var list: List<LeavesItem>?
) :
    RecyclerView.Adapter<LeaveAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = LeaveItemListBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.eName.text = list?.get(position)?.employee?.user?.name
        holder.binding.eCode.text = "CFT00"+list?.get(position)?.employee?.employeeCode
        holder.binding.reasons.text = list?.get(position)?.title
        holder.binding.dates.text = FunctionClass.changeDate(list?.get(position)?.leaveFrom)+" - "+FunctionClass.changeDate(list?.get(position)?.leaveEnd)
        holder.binding.applyDate.text = FunctionClass.changeDate(list?.get(position)?.createdAt)
        holder.binding.leaveType.text ="For "+list?.get(position)?.leaveDays+" days | Full Days"

        Glide.with(context).load(Constants.IMG_URL+list?.get(position)?.employee?.user?.profile_img).placeholder(R.drawable.person).into(holder.binding.userImage)


        if(list?.get(position)?.status=="approved"){
            holder.binding.status.setTextColor(context.getColor(R.color.pulse_color))
            holder.binding.status.text ="Approved"
            holder.binding.approveBy.visibility=View.VISIBLE
        }else if(list?.get(position)?.status=="rejected"){
            holder.binding.status.setTextColor(context.getColor(R.color.red))
            holder.binding.status.text ="Rejected"
            holder.binding.approveBy.visibility=View.GONE
        }else{
            holder.binding.status.setTextColor(context.getColor(R.color.orange))
            holder.binding.status.text ="Pending"
            holder.binding.approveBy.visibility=View.GONE
        }

        holder.binding.leaveDetails.setOnClickListener {
            LeaveDetailsActivity.leave=list!!.get(position)
            context.startActivity(
                Intent(
                context,
                LeaveDetailsActivity::class.java
            )
            )
        }
    }




    override fun getItemCount(): Int {
        return list!!.size
    }



    inner class MyViewHolder(val binding: LeaveItemListBinding) : RecyclerView.ViewHolder(binding.root)

}