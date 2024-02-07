package codeflies.com.pulse.Notifications.Adapters

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import codeflies.com.pulse.Helpers.FunctionClass
import codeflies.com.pulse.Models.NotificationsItem
import codeflies.com.pulse.R
import codeflies.com.pulse.databinding.NotificationItemsBinding

class NotificationAdapter(private val dataList: List<NotificationsItem>?, var ctx: Context) :
    RecyclerView.Adapter<NotificationAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_items, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {


        holder.binding.title.text= Html.fromHtml(dataList?.get(position)?.data?.title, Html.FROM_HTML_MODE_LEGACY)
        holder.binding.desc.text= Html.fromHtml(dataList?.get(position)?.data?.text, Html.FROM_HTML_MODE_LEGACY)
        holder.binding.date.text= FunctionClass.changeDate(dataList?.get(position)?.createdAt)

        if(dataList?.get(position)?.readAt==null){
            holder.binding.readStatus.visibility=View.VISIBLE
        }else{
            holder.binding.readStatus.visibility=View.GONE
        }
    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = NotificationItemsBinding.bind(itemView)
    }
}
