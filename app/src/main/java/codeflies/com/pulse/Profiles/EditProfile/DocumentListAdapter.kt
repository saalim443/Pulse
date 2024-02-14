package com.codeflies.supertravel.TabsLayou.TabLayoutFragment.UpComingRides

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import codeflies.com.pulse.Candidate.cadidatedetail.CandidateDetailActivity
import codeflies.com.pulse.Helpers.Constants
import codeflies.com.pulse.Helpers.FunctionClass
import codeflies.com.pulse.Intro.IntroScreenActivity
import codeflies.com.pulse.Models.Candidates.CandidatesItem
import codeflies.com.pulse.Models.Leaves.LeavesItem
import codeflies.com.pulse.Models.Profile.DocumentsItem
import codeflies.com.pulse.Models.UserData.ManagersItem
import codeflies.com.pulse.R
import codeflies.com.pulse.databinding.CandidateItemListBinding
import codeflies.com.pulse.databinding.DocumentListItemBinding
import codeflies.com.pulse.databinding.LeaveItemListBinding
import codeflies.com.pulse.databinding.ManagerListItemBinding

import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*


class DocumentListAdapter(
    val context: Context, private var list: List<DocumentsItem>?
) :
    RecyclerView.Adapter<DocumentListAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = DocumentListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.binding.name.text=list?.get(position)?.title
        holder.binding.type.text=list?.get(position)?.docType
        holder.binding.remark.text=list?.get(position)?.remarks

        Glide.with(context)
            .load(Constants.IMG_URL +list?.get(position)?.filePath)
            .placeholder(R.drawable.logo).into(holder.binding.image)


        holder.binding.download.setOnClickListener {
            FunctionClass.downloadFile(context, "Document",Constants.IMG_URL+ list?.get(position)?.filePath) // Change the URL to your actual download link
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    inner class MyViewHolder(val binding: DocumentListItemBinding) : RecyclerView.ViewHolder(binding.root)

}