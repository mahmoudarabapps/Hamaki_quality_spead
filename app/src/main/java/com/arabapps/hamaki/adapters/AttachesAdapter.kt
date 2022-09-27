package com.arabapps.hamaki.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arabapps.hamaki.R
import com.arabapps.hamaki.WebViewActivity
import com.arabapps.hamaki.data.FilesItem
import com.arabapps.hamaki.databinding.ItemAttatchesBinding


private const val TAG = "NewsAdapter"

class AttachesAdapter : RecyclerView.Adapter<AttachesAdapter.ViewHolder>() {
    var files: List<FilesItem?> = ArrayList()

    inner class ViewHolder(val binding: ItemAttatchesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.textDownload.setOnClickListener {
                if (adapterPosition < 0)
                    return@setOnClickListener
                val intent = Intent(binding.root.context, WebViewActivity::class.java)
                intent.putExtra("url", files.get(adapterPosition)?.filePath)
                intent.putExtra("name", files.get(adapterPosition)?.name)
                binding.root.context.startActivity(intent)

            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachesAdapter.ViewHolder {

        return ViewHolder(
            ItemAttatchesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return files?.size
    }

    override fun onBindViewHolder(holder: ViewHolder, p: Int) {
        if (p % 2 == 0) {
            holder.binding.root.setBackgroundColor(holder.binding.root.context.resources.getColor(R.color.gray))
        } else holder.binding.root.setBackgroundColor(Color.WHITE)

        holder.binding.textTitle.text = files.get(p)?.name
        holder.binding.textNumber.text = (p + 1).toString()

    }

    fun setAll(
        files: List<FilesItem?>?
    ) {
        this.files = files!!

        notifyDataSetChanged()
    }

}