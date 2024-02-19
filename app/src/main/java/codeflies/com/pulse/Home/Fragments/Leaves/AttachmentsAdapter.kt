package codeflies.com.pulse.Home.Fragments.Leaves

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import codeflies.com.pulse.Helpers.Constants
import codeflies.com.pulse.Helpers.FunctionClass
import codeflies.com.pulse.Models.Leaves.attachmentsItem
import codeflies.com.pulse.databinding.ItemAttachmentBinding
import com.bumptech.glide.Glide


class AttachmentsAdapter(
    val context: Context,
    private val attachments: List<attachmentsItem>
) :
    RecyclerView.Adapter<AttachmentsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAttachmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val attachment = attachments[position]
        Glide.with(context).load(Constants.IMG_URL+attachment.file_path).into(holder.binding.textFilePath)
        holder.binding.download.setOnClickListener {
            FunctionClass.downloadFile(context,"Attachment", Constants.IMG_URL+attachment.file_path);
        }
    }

    override fun getItemCount(): Int {
        return attachments.size
    }

    inner class ViewHolder(val binding: ItemAttachmentBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}
