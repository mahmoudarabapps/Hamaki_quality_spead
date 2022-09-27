package com.arabapps.hamaki.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.arabapps.hamaki.BuildConfig
import com.arabapps.hamaki.R
import com.arabapps.hamaki.data.SubjectsItem
import com.arabapps.hamaki.databinding.ItemAllSubjectsBinding
import com.arabapps.hamaki.helper.HtmlHelper
import com.bumptech.glide.Glide


private const val TAG = "NewsAdapter"

class AllSubjectsAdapter : RecyclerView.Adapter<AllSubjectsAdapter.ViewHolder>(), Filterable {

    var subjects: List<SubjectsItem?> = ArrayList()
    var subjectsCopy: List<SubjectsItem?> = ArrayList()

    inner class ViewHolder(val binding: ItemAllSubjectsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.constrains.setOnClickListener {
                if (adapterPosition < 0)
                    return@setOnClickListener
                val bundle = Bundle()
                subjects.get(adapterPosition)?.id?.let { it1 -> bundle.putInt("id", it1) }
                Navigation.createNavigateOnClickListener(
                    R.id.action_navigation_subjects_to_subjectContentFragment,
                    bundle
                )
                    .onClick(binding.constrains)
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        return ViewHolder(
            ItemAllSubjectsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return subjects?.size
    }

    override fun onBindViewHolder(holder: ViewHolder, p: Int) {
        holder.binding.textTitle.text = subjects.get(p)?.name
        holder.binding.textDesc.text = HtmlHelper.getString( subjects.get(p)?.description.toString())
        Glide.with(holder.binding.roundedImageView).load(""+subjects.get(p)?.imagePath)
            .placeholder(R.drawable.no_image)
            .into(holder.binding.roundedImageView)
    }

    fun setAll(subjects: List<SubjectsItem?>?) {
        if (subjects != null) {
            this.subjects = subjects
            subjectsCopy = this.subjects
        }
        notifyDataSetChanged()

    }

    override fun getFilter(): Filter {
        return object : Filter() {


            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (BuildConfig.DEBUG)  Log.d(TAG, "performFiltering: seaching for $charString")
                if (charString.trim().isEmpty()) {
                    subjects = subjectsCopy
                } else {
                    val filteredList: MutableList<SubjectsItem?> = ArrayList()
                    for (row in subjectsCopy) {

                        if (BuildConfig.DEBUG)  Log.d(TAG, "performFiltering: ${row?.description} ")
                        if (row?.description.toString()?.toLowerCase()
                                ?.contains(charString.toLowerCase())!! ||
                            row?.name.toString()?.toLowerCase()
                                ?.contains(charString.toLowerCase())!!
                        ) {
                            filteredList.add(row)
                        }
                    }
                    subjects = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = subjects
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                subjects = filterResults.values as List<SubjectsItem?>
                // refresh the list with filtered data
                notifyDataSetChanged()
            }
        }

    }
}