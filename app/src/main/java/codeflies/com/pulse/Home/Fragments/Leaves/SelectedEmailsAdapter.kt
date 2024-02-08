package codeflies.com.pulse.Home.Fragments.Leaves


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import codeflies.com.pulse.databinding.AllEmailBinding

class SelectedEmailsAdapter(
    private val imageList: MutableList<String>,
    private val onDeleteClickListener: (Int) -> Unit
) : RecyclerView.Adapter<SelectedEmailsAdapter.ImageViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AllEmailBinding.inflate(inflater, parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.binding.emailAll.text = imageList[position].toString()
        holder.binding.CarImageDelete.setOnClickListener {
            onDeleteClickListener.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    class ImageViewHolder(val binding: AllEmailBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun removeImage(position: Int) {
        if (position in 0 until imageList.size) {
            imageList.removeAt(position)
            notifyDataSetChanged()
        }
    }
}
