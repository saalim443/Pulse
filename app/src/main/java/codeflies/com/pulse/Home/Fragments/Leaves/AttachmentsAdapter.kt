package codeflies.com.pulse.Home.Fragments.Leaves

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import codeflies.com.pulse.Helpers.Constants
import codeflies.com.pulse.Models.Leaves.attachmentsItem
import codeflies.com.pulse.databinding.ItemAttachmentBinding
import com.bumptech.glide.Glide


class AttachmentsAdapter(
    context: LeaveDetailsActivity,
    private val attachments: List<attachmentsItem>
) :
    RecyclerView.Adapter<AttachmentsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAttachmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val attachment = attachments[position]
        holder.bind(attachment)
    }

    override fun getItemCount(): Int {
        return attachments.size
    }

    inner class ViewHolder(private val binding: ItemAttachmentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(attachment: attachmentsItem) {
          //  binding.textFilePath.text = "File Path: ${attachment.file_path}"
            Glide.with(binding.root.context).load(Constants.IMG_URL+attachment.file_path).into(binding.textFilePath)
        }
    }
}
