package com.codeflies.supertravel.TabsLayou.TabLayoutFragment.UpComingRides

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import codeflies.com.pulse.Helpers.FunctionClass
import codeflies.com.pulse.Models.CandidateDetails.CandidateRoundsItem
import codeflies.com.pulse.Models.CandidateDetails.CommentsItem
import codeflies.com.pulse.databinding.CommentItemListBinding
import codeflies.com.pulse.databinding.InterviewlistItemBinding


class InterviewAdapter(
    val context: Context, private var list: List<CandidateRoundsItem>?
) :
    RecyclerView.Adapter<InterviewAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = InterviewlistItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        holder.binding.interviewer.text=list?.get(position)?.interviewer?.name
        holder.binding.date.text=FunctionClass.changeDate(list?.get(position)?.interviewAt)
        holder.binding.status.text=list?.get(position)?.status?.capitalize()+" | "
        holder.binding.round.text=list?.get(position)?.round
    }




    override fun getItemCount(): Int {
        return list!!.size
    }



    inner class MyViewHolder(val binding: InterviewlistItemBinding) : RecyclerView.ViewHolder(binding.root)

}