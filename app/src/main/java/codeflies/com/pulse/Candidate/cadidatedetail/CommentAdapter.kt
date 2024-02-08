package com.codeflies.supertravel.TabsLayou.TabLayoutFragment.UpComingRides

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import codeflies.com.pulse.Helpers.FunctionClass
import codeflies.com.pulse.Models.CandidateDetails.CommentsItem
import codeflies.com.pulse.databinding.CommentItemListBinding


class CommentAdapter(
    val context: Context, private var list: List<CommentsItem>?
) :
    RecyclerView.Adapter<CommentAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CommentItemListBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.msg.text = list?.get(position)?.comment
        holder.binding.name.text = list?.get(position)?.user?.name+":"
        holder.binding.date.text = FunctionClass.changeDate(list?.get(position)?.createdAt)


    }




    override fun getItemCount(): Int {
        return list!!.size
    }



    inner class MyViewHolder(val binding: CommentItemListBinding) : RecyclerView.ViewHolder(binding.root)

}