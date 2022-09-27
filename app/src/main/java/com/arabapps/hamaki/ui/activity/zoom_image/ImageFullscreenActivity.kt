package com.arabapps.hamaki.ui.activity.zoom_image

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.arabapps.hamaki.databinding.ActivityImageFullscreenBinding
import com.arabapps.hamaki.helper.ImageViewHelper.Companion.loadImage


class ImageFullscreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageFullscreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        binding = ActivityImageFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.zoomableImage.loadImage(intent.getStringExtra("image"))
        binding.back.setOnClickListener {
            finish()
        }
    }

}