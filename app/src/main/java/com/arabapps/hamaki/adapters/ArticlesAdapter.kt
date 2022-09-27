package com.arabapps.hamaki.adapters

import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.arabapps.hamaki.R
import com.arabapps.hamaki.data.DataItem
import com.arabapps.hamaki.databinding.ItemArticlesBinding
import com.arabapps.hamaki.helper.HtmlHelper
import com.arabapps.hamaki.helper.TimeHelper
import com.arabapps.hamaki.ui.activity.zoom_image.ImageFullscreenActivity
import com.bumptech.glide.Glide


private const val TAG = "NewsAdapter"

class ArticlesAdapter(val postReaction: PostReaction) :
    RecyclerView.Adapter<ArticlesAdapter.ViewHolder>() {

    interface PostReaction {
        fun react(id: Int, position: Int, reaction: Int, isReacted: Int)
        fun delete(id: Int, position: Int)
        fun openReacts(id: Int)
    }

    var data: ArrayList<DataItem?> = ArrayList()
    var dataCopy: ArrayList<DataItem?> = ArrayList()

    inner class ViewHolder(val binding: ItemArticlesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.imageView.setOnClickListener {
                if (adapterPosition == -1)
                    return@setOnClickListener
                val intent = Intent(it.context, ImageFullscreenActivity::class.java)
                intent.putExtra("image", data[adapterPosition]?.image_path)
                it.context.startActivity(intent)
            }
            binding.root.setOnClickListener {
                if (adapterPosition == -1)
                    return@setOnClickListener
                data[adapterPosition]?.id?.let { it1 ->
                    postReaction.openReacts(it1)
                }
            }
            binding.imageLike.setOnClickListener {
                if (adapterPosition == -1)
                    return@setOnClickListener
                if (data[adapterPosition]?.student_reaction != null &&
                    data[adapterPosition]?.student_reaction == 0
                ) {
                    data[adapterPosition]?.likes_count =
                        data[adapterPosition]?.likes_count?.minus(1)
                    data[adapterPosition]?.student_reaction = null
                    data[adapterPosition]?.id?.let { it1 ->
                        postReaction.delete(
                            it1,
                            adapterPosition
                        )
                    }
                    return@setOnClickListener

                } else if (data[adapterPosition]?.student_reaction != null &&
                    data[adapterPosition]?.student_reaction == 1
                ) {
                    data[adapterPosition]?.loves_count =
                        data[adapterPosition]?.loves_count?.minus(1)


                } else if (data[adapterPosition]?.student_reaction != null &&
                    data[adapterPosition]?.student_reaction == 2
                ) {
                    data[adapterPosition]?.dislikes_count =
                        data[adapterPosition]?.dislikes_count?.minus(1)


                }


                data[adapterPosition]?.likes_count =
                    data[adapterPosition]?.likes_count?.plus(1)


                data[adapterPosition]?.student_reaction = 0
                data[adapterPosition]?.id?.let { it1 ->
                    postReaction.react(
                        it1,
                        adapterPosition,
                        0,
                        -1
                    )
                }
            }
            binding.imageLove.setOnClickListener {
                if (adapterPosition == -1)
                    return@setOnClickListener
                if (data[adapterPosition]?.student_reaction != null &&
                    data[adapterPosition]?.student_reaction == 1
                ) {
                    data[adapterPosition]?.loves_count =
                        data[adapterPosition]?.loves_count?.minus(1)
                    data[adapterPosition]?.student_reaction = null
                    data[adapterPosition]?.id?.let { it1 ->
                        postReaction.delete(
                            it1,
                            adapterPosition
                        )
                    }
                    return@setOnClickListener

                } else if (data[adapterPosition]?.student_reaction != null &&
                    data[adapterPosition]?.student_reaction == 0
                ) {
                    data[adapterPosition]?.likes_count =
                        data[adapterPosition]?.likes_count?.minus(1)

                } else if (data[adapterPosition]?.student_reaction != null &&
                    data[adapterPosition]?.student_reaction == 2
                ) {
                    data[adapterPosition]?.dislikes_count =
                        data[adapterPosition]?.dislikes_count?.minus(1)

                }
                data[adapterPosition]?.loves_count =
                    data[adapterPosition]?.loves_count?.plus(1)
                data[adapterPosition]?.student_reaction = 1
                data[adapterPosition]?.id?.let { it1 ->
                    postReaction.react(
                        it1,
                        adapterPosition,
                        1,
                        -1
                    )
                }
            }
            binding.imageSad.setOnClickListener {
                if (adapterPosition == -1)
                    return@setOnClickListener
                if (data[adapterPosition]?.student_reaction != null &&
                    data[adapterPosition]?.student_reaction == 2
                ) {
                    data[adapterPosition]?.dislikes_count =
                        data[adapterPosition]?.dislikes_count?.minus(1)
                    data[adapterPosition]?.student_reaction = null
                    data[adapterPosition]?.id?.let { it1 ->
                        postReaction.delete(
                            it1,
                            adapterPosition
                        )
                    }
                    return@setOnClickListener

                } else if (data[adapterPosition]?.student_reaction != null &&
                    data[adapterPosition]?.student_reaction == 0
                ) {
                    data[adapterPosition]?.likes_count =
                        data[adapterPosition]?.likes_count?.minus(1)

                } else if (data[adapterPosition]?.student_reaction != null && data[adapterPosition]?.student_reaction == 1) {
                    data[adapterPosition]?.loves_count =
                        data[adapterPosition]?.loves_count?.minus(1)
                }
                data[adapterPosition]?.dislikes_count =
                    data[adapterPosition]?.dislikes_count?.plus(1)
                data[adapterPosition]?.student_reaction = 2
                data[adapterPosition]?.id?.let { it1 ->
                    postReaction.react(
                        it1,
                        adapterPosition,
                        2,
                        -1
                    )
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemArticlesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    private fun setTextViewDrawableColor(textView: TextView, color: Int) {
        for (drawable in textView.compoundDrawables) {
            if (drawable != null) {
                drawable.colorFilter = PorterDuffColorFilter(
                    ContextCompat.getColor(
                        textView.context,
                        color
                    ), PorterDuff.Mode.SRC_IN
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return data?.size
    }

    override fun onBindViewHolder(holder: ViewHolder, p: Int) {


        holder.binding.textUsername.text = data[p]?.doctor?.fullName
        holder.binding.imageLike.text = data[p]?.likes_count.toString()
        holder.binding.imageLove.text = data[p]?.loves_count.toString()
        holder.binding.imageSad.text = data[p]?.dislikes_count.toString()

        holder.binding.textDesc.text = HtmlHelper.getString(data[p]?.description.toString())
        holder.binding.textTime.text =
            TimeHelper.formatTime(data[p]?.created_at.toString())
        Glide.with(holder.binding.circleImage).load("" + data[p]?.doctor?.imagePath)
            .placeholder(R.drawable.default_user)
            .into(holder.binding.circleImage)
        if (data[p]?.image_path.isNullOrEmpty()) {
            holder.binding.imageView.visibility = View.GONE
        } else {
            holder.binding.imageView.visibility = View.VISIBLE
            Glide.with(holder.binding.imageView).load("" + data[p]?.image_path)
                .placeholder(R.drawable.no_image)
                .into(holder.binding.imageView)
        }

        holder.binding.imageLike.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_like,
            0,
            0,
            0
        );
        holder.binding.imageLove.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_love,
            0,
            0,
            0
        );
        holder.binding.imageSad.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sad, 0, 0, 0);



        if (data[p]?.student_reaction == 0)
            holder.binding.imageLike.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_like_green,
                0,
                0,
                0
            );
        else if (data[p]?.student_reaction == 1)
            holder.binding.imageLove.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_love_green,
                0,
                0,
                0
            );
        else if (data[p]?.student_reaction == 2)
            holder.binding.imageSad.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_sad_green,
                0,
                0,
                0
            );

    }

    fun setAll(data: List<DataItem?>?) {
        this.data.clear()
        if (data != null) {
            this.data = data as ArrayList<DataItem?>
            this.dataCopy = this.data
        }
        notifyDataSetChanged()
    }


    fun addAll(data: List<DataItem?>?) {

        if (data != null) {
            this.data.addAll(data)
            this.dataCopy = this.data
        }
        notifyDataSetChanged()
    }

    fun notifyItemChange(adapterPosition: Int) {
        notifyItemChanged(adapterPosition)
    }

    fun deleteReaction(adapterPosition: Int) {

        notifyItemChanged(adapterPosition)

    }


}