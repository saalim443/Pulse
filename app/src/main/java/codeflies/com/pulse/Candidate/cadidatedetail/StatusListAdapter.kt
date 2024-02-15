package com.codeflies.supertravel.TabsLayou.TabLayoutFragment.UpComingRides

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.RecyclerView
import codeflies.com.pulse.Helpers.FunctionClass
import codeflies.com.pulse.Helpers.Interfaces.RefreshStatus
import codeflies.com.pulse.Models.CandidateDetails.CandidateRoundsItem
import codeflies.com.pulse.Models.CandidateDetails.CommentsItem
import codeflies.com.pulse.Models.Candidates.CandidateStatusItem
import codeflies.com.pulse.databinding.CommentItemListBinding
import codeflies.com.pulse.databinding.PopupStatusListBinding


class StatusListAdapter( var popupWindow: PopupWindow,var refreshStatus: RefreshStatus,
    val context: Context, private var list: List<CandidateStatusItem>?
) :
    RecyclerView.Adapter<StatusListAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = PopupStatusListBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.binding.status.text=list?.get(position)?.name

        holder.binding.status.setOnClickListener {
            popupWindow.dismiss()
            refreshStatus.onRefresh(list?.get(position)?.slug!!,list?.get(position)?.name!!)
        }
    }




    override fun getItemCount(): Int {
        return list!!.size
    }



    inner class MyViewHolder(val binding: PopupStatusListBinding) : RecyclerView.ViewHolder(binding.root)



}