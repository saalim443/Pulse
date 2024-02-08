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
import codeflies.com.pulse.Intro.IntroScreenActivity
import codeflies.com.pulse.Models.Candidates.CandidatesItem
import codeflies.com.pulse.Models.Leaves.LeavesItem
import codeflies.com.pulse.Models.UserData.ManagersItem
import codeflies.com.pulse.R
import codeflies.com.pulse.databinding.CandidateItemListBinding
import codeflies.com.pulse.databinding.LeaveItemListBinding
import codeflies.com.pulse.databinding.ManagerListItemBinding

import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*


class ManagersListAdapter(
    val context: Context, private var list: List<ManagersItem>?
) :
    RecyclerView.Adapter<ManagersListAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ManagerListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.manager.text = list?.get(position)?.name
        holder.binding.managerEmail.text = list?.get(position)?.email
        holder.binding.sn.text = (position+1).toString()

    }




    override fun getItemCount(): Int {
        return list!!.size
    }



    inner class MyViewHolder(val binding: ManagerListItemBinding) : RecyclerView.ViewHolder(binding.root)

}