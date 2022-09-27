package com.arabapps.hamaki.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arabapps.hamaki.R
import com.arabapps.hamaki.data.NotificationDataItem
import com.arabapps.hamaki.databinding.ItemArticlesBinding
import com.arabapps.hamaki.helper.HtmlHelper
import com.arabapps.hamaki.helper.TimeHelper
import com.bumptech.glide.Glide


private const val TAG = "NewsAdapter"

class NotificationsAdapter : RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {
    val notificationData: ArrayList<NotificationDataItem?> = ArrayList()

    inner class ViewHolder(val binding: ItemArticlesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.imageLike.visibility = View.GONE
            binding.imageLove.visibility = View.GONE
            binding.imageSad.visibility = View.GONE
            binding.circleImage.visibility = View.GONE

            binding.root.setOnClickListener {

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position + 1
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationsAdapter.ViewHolder {

        return ViewHolder(
            ItemArticlesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return notificationData.size
    }

    override fun onBindViewHolder(holder: NotificationsAdapter.ViewHolder, p: Int) {
        holder.binding.textUsername.text = notificationData.get(p)?.title
        holder.binding.textDesc.text =
            HtmlHelper.getString(notificationData.get(p)?.content.toString())
        holder.binding.textTime.text =
            TimeHelper.formatTime(notificationData.get(p)?.createdat.toString())
        Glide.with(holder.binding.imageView).load(""+notificationData.get(p)?.imagePath)
            .placeholder(R.drawable.no_image)
            .into(holder.binding.imageView)
    }

    fun setAll(notificationData: List<NotificationDataItem?>?) {
        this.notificationData.clear()
        if (notificationData != null) {
            this.notificationData.addAll(notificationData)
            notifyDataSetChanged()
        }

    }

    fun addAll(notificationData: List<NotificationDataItem?>?) {
        if (notificationData != null) {
            val size = this.notificationData.size
            this.notificationData.addAll(notificationData)
            notifyItemRangeInserted(size, this.notificationData.size-1)
        }

    }


}