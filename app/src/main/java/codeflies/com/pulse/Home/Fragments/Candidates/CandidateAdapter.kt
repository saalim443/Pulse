package com.codeflies.supertravel.TabsLayou.TabLayoutFragment.UpComingRides

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import codeflies.com.pulse.Helpers.FunctionClass
import codeflies.com.pulse.Models.Candidates.CandidatesItem
import codeflies.com.pulse.R
import codeflies.com.pulse.databinding.CandidateItemListBinding
import codeflies.com.pulse.Candidate.cadidatedetail.CandidateDetailActivity


class CandidateAdapter(
    val context: Context, private var list: List<CandidatesItem>?
) :
    RecyclerView.Adapter<CandidateAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CandidateItemListBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.eName.text = list?.get(position)?.name
        holder.binding.mobile.text = list?.get(position)?.mobile
        holder.binding.email.text = list?.get(position)?.email
        holder.binding.date.text = FunctionClass.changeDate(list?.get(position)?.createdAt)


        holder.itemView.setOnClickListener{
            CandidateDetailActivity.candidate=list!!.get(position)
            context.startActivity(Intent(
                context,
                CandidateDetailActivity::class.java
            ))
        }

        if(list?.get(position)?.status=="in_progress"){
            holder.binding.status.setTextColor(context.getColor(R.color.orange))
            holder.binding.status.text ="In Progress"
        }else if(list?.get(position)?.status=="selected"){
            holder.binding.status.setTextColor(context.getColor(R.color.pulse_color))
            holder.binding.status.text ="Selected"
        }else if(list?.get(position)?.status=="rejected"){
            holder.binding.status.setTextColor(context.getColor(R.color.red))
            holder.binding.status.text ="Rejected"
        }else{
            holder.binding.status.setTextColor(context.getColor(R.color.orange))
            holder.binding.status.text ="Not Interested"
        }

    }




    override fun getItemCount(): Int {
        return list!!.size
    }



    inner class MyViewHolder(val binding: CandidateItemListBinding) : RecyclerView.ViewHolder(binding.root)

}