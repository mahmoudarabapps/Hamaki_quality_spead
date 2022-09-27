package com.arabapps.hamaki.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arabapps.hamaki.R
import com.arabapps.hamaki.data.ReactionDataItem
import com.arabapps.hamaki.databinding.ItemStudentsBinding
import com.bumptech.glide.Glide


private const val TAG = "NewsAdapter"

class ReactsStudentsAdapter() :
    RecyclerView.Adapter<ReactsStudentsAdapter.ViewHolder>() {

    private val data = ArrayList<ReactionDataItem?>()

    inner class ViewHolder(val binding: ItemStudentsBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        return ViewHolder(
            ItemStudentsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, p: Int) {
        holder.binding.tvUsername.text = data?.get(p)?.fullName
        Glide.with(holder.binding.civ).load("" + data?.get(p)?.imagePath)
            .placeholder(R.drawable.no_image)
            .into(holder.binding.civ)
    }

    fun add(data: List<ReactionDataItem?>?) {
        this.data.apply {
            data?.let { addAll(it) }
            notifyDataSetChanged()
        }
    }

    fun set(dataList: List<ReactionDataItem?>?) {

        this.data.apply {
            clear()
            dataList?.let { addAll(it) }
            notifyDataSetChanged()
        }
    }


    fun removeAll() {
        this.data.clear()
        notifyDataSetChanged()
    }


}