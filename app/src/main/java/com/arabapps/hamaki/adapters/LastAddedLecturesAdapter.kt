package com.arabapps.hamaki.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.arabapps.hamaki.R
import com.arabapps.hamaki.data.LastLectureResponse
import com.arabapps.hamaki.databinding.ItemLastAddedHomeBinding
import com.bumptech.glide.Glide


private const val TAG = "NewsAdapter"

class LastAddedLecturesAdapter : RecyclerView.Adapter<LastAddedLecturesAdapter.ViewHolder>(),
    Filterable {

    var data: ArrayList<LastLectureResponse> = ArrayList()
    var dataCopy: ArrayList<LastLectureResponse> = ArrayList()

    inner class ViewHolder(val binding: ItemLastAddedHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (adapterPosition < 0)
                    return@setOnClickListener
                val bundle = Bundle()
                data.get(adapterPosition)?.id?.let { it1 -> bundle.putInt("id", it1) }

                Navigation.createNavigateOnClickListener(
                    R.id.action_navigation_home_to_lectureFragment,
                    bundle
                )
                    .onClick(it)
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LastAddedLecturesAdapter.ViewHolder {

        return ViewHolder(
            ItemLastAddedHomeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return data?.size
    }

    override fun onBindViewHolder(holder: LastAddedLecturesAdapter.ViewHolder, p: Int) {
        holder.binding.textTitle.text = data.get(p)?.name

        holder.binding.textPrice.text = data.get(p)?.subject?.name

        Glide.with(holder.binding.imageView).load(""+data.get(p)?.image_path)
            .placeholder(R.drawable.no_image)
            .into(holder.binding.imageView)
    }

    fun setAll(it: List<LastLectureResponse>?) {
        data = it as ArrayList<LastLectureResponse>
        dataCopy = data
        notifyDataSetChanged()
    }


    override fun getFilter(): Filter {
        return object : Filter() {


            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.trim().isEmpty()) {
                    data = dataCopy
                } else {
                    val filteredList: ArrayList<LastLectureResponse> = ArrayList()
                    for (row in dataCopy) {


                        if (row?.name.toString()?.toLowerCase()
                                ?.contains(charString.toLowerCase())!! ||
                            row?.subject?.name.toString()?.toLowerCase()
                                ?.contains(charString.toLowerCase())!! ||
                            row?.subject?.description.toString()?.toLowerCase()
                                ?.contains(charString.toLowerCase())!!
                        ) {
                            filteredList.add(row)
                        }
                    }
                    data = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = data
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                data = filterResults.values as ArrayList<LastLectureResponse>
                // refresh the list with filtered data
                notifyDataSetChanged()
            }
        }

    }

}