package com.arabapps.hamaki.adapters

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.arabapps.hamaki.R
import com.arabapps.hamaki.data.SubjectsItem
import com.arabapps.hamaki.databinding.ItemAllSubjectsHomeBinding
import com.arabapps.hamaki.ui.activity.zoom_image.ImageFullscreenActivity
import com.bumptech.glide.Glide


private const val TAG = "NewsAdapter"

class AllSubjectsHomeAdapter : RecyclerView.Adapter<AllSubjectsHomeAdapter.ViewHolder>(),Filterable {

    var subjects: List<SubjectsItem?> = ArrayList()
    var subjectsCopy: List<SubjectsItem?> = ArrayList()

    inner class ViewHolder(val binding: ItemAllSubjectsHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

            binding.root.setOnClickListener {
                if (adapterPosition < 0)
                    return@setOnClickListener
                val bundle = Bundle()
                subjects[adapterPosition]?.id?.let { it1 -> bundle.putInt("id", it1) }
                Navigation.createNavigateOnClickListener(
                    R.id.action_navigation_home_to_subjectContentFragment,
                    bundle
                )
                    .onClick(binding.root)
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        return ViewHolder(
            ItemAllSubjectsHomeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return Math.min(subjects?.size, 6)
    }

    override fun onBindViewHolder(holder: ViewHolder, p: Int) {

        holder.binding.textTitle.text = subjects.get(p)?.name
        Glide.with(holder.binding.imageView).load(""+subjects.get(p)?.imagePath).placeholder(R.drawable.no_image)
            .into(holder.binding.imageView)
    }

    fun setAll(subjects: List<SubjectsItem?>?) {
        if (subjects != null) {
            this.subjects = subjects
            this.subjectsCopy=subjects
        }
        notifyDataSetChanged()
    }
    override fun getFilter(): Filter {
        return object : Filter() {


            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.trim().isEmpty()) {
                    subjects = subjectsCopy
                } else {
                    val filteredList: MutableList<SubjectsItem?> = ArrayList()
                    for (row in subjectsCopy) {


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
                subjects = filterResults.values as ArrayList<SubjectsItem?>
                // refresh the list with filtered data
                notifyDataSetChanged()
            }
        }

    }

}