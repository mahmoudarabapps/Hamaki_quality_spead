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
import com.arabapps.hamaki.data.LecturesItem
import com.arabapps.hamaki.databinding.ItemFavBinding
import com.arabapps.hamaki.helper.HtmlHelper
import com.bumptech.glide.Glide


private const val TAG = "NewsAdapter"

class FavAdapter(val itemclick: ItemClicked) : RecyclerView.Adapter<FavAdapter.ViewHolder>() ,Filterable{
    var lectures: ArrayList<LecturesItem?> = ArrayList()
    var lecturesCopy: ArrayList<LecturesItem?> = ArrayList()

    inner class ViewHolder(val binding: ItemFavBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (adapterPosition < 0)
                    return@setOnClickListener
                val bundle = Bundle()
                lectures.get(adapterPosition)?.id?.let { it1 -> bundle.putInt("id", it1) }
                Navigation.createNavigateOnClickListener(
                    R.id.action_subjectContentFragment_to_lectureFragment,
                    bundle
                )
                    .onClick(it)
            }
            binding.imageView.setOnClickListener {
                if (adapterPosition < 0)
                    return@setOnClickListener
                binding.imageView.alpha = .3f
                binding.imageView.setEnabled(false)
                if (lectures.get(adapterPosition)?.bookmark?.contains("1")!!) {
                    lectures.get(adapterPosition)!!.id?.let { it1 ->
                        itemclick.removeFav(
                            it1,
                            adapterPosition
                        )
                    }
                } else {
                    lectures.get(adapterPosition)!!.id?.let { it1 ->
                        itemclick.addToFav(
                            it1,
                            adapterPosition
                        )
                    }

                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavAdapter.ViewHolder {

        return ViewHolder(
            ItemFavBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return lectures?.size
    }

    override fun onBindViewHolder(holder: FavAdapter.ViewHolder, p: Int) {

        holder.binding.imageView.alpha = 1f
        holder.binding.imageView.isEnabled = true

        holder.binding.textTitle.text = lectures[p]?.name?.trim()
        holder.binding.textDesc.text = HtmlHelper.getString(lectures[p]?.description.toString().trim().replace("\n",""))
        Glide.with(holder.binding.roundedImageView).load(""+lectures[p]?.image_path)
            .placeholder(R.drawable.no_image)
            .into(holder.binding.roundedImageView)
        if (lectures[p]?.bookmark?.contains("1")!!)
            holder.binding.imageView.setImageResource(R.drawable.baseline_bookmark_orange_300_24dp)
        else
            holder.binding.imageView.setImageResource(R.drawable.ic_bookmark_unfill)

    }

    fun setAll(lectures: ArrayList<LecturesItem?>?) {
        this.lectures = lectures!!
        this.lecturesCopy=this.lectures
        notifyDataSetChanged()
    }

    fun addAll(data: ArrayList<LecturesItem?>?) {
        this.lectures.addAll(data!!)
        this.lecturesCopy=this.lectures
        notifyDataSetChanged()
    }

    fun itemAdded(positon: Int) {
        try {
            lectures[positon]?.bookmark = "1"
            notifyItemChanged(positon)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    fun itemRemove(positon: Int) {
        try {
            lectures.get(positon)?.bookmark = "0"
            notifyItemChanged(positon)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun deleteItem(position: Int) {

        try {
            lectures.removeAt(position)
            notifyItemRemoved(position)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    interface ItemClicked {
        fun addToFav(id: Int, position: Int)
        fun removeFav(id: Int, position: Int)

    }
    override fun getFilter(): Filter {
        return object : Filter() {


            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (BuildConfig.DEBUG)  Log.d(TAG, "performFiltering: "+lecturesCopy.size)
                if (charString.toString().trim().isEmpty()) {
                    lectures = lecturesCopy
                } else {
                    val filteredList: ArrayList<LecturesItem?> = ArrayList()
                    for (row in lecturesCopy) {


                        if (row?.description.toString()?.toLowerCase()
                                ?.contains(charString.toLowerCase())!! ||
                            row?.name.toString()?.toLowerCase()
                                ?.contains(charString.toLowerCase())!!
                        ) {
                            filteredList.add(row)
                        }
                    }
                    lectures = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = lectures
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                lectures = filterResults.values as ArrayList<LecturesItem?>
                // refresh the list with filtered data
                notifyDataSetChanged()
            }
        }

    }
}