package com.arabapps.hamaki.helper

import android.widget.ImageView
import androidx.core.view.isVisible
import com.arabapps.hamaki.R
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority

class ImageViewHelper {
    companion object {

        fun ImageView.loadImageAndHide(url: String?) {
            this.isVisible = url.isNullOrEmpty()
            if (!url.isNullOrEmpty())
                Glide.with(this).load(url).priority(Priority.HIGH)
                    .override(700, 700)
                    .placeholder(R.drawable.no_image).into(this)
        }

        fun ImageView.loadImage(url: String?) {
            Glide.with(this).load(url).priority(Priority.HIGH)
                .override(700, 700)
                .placeholder(R.drawable.no_image).into(this)
        }
    }
}