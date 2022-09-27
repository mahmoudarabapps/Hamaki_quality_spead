package com.arabapps.hamaki

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arabapps.hamaki.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.imageDownload.setOnClickListener {


            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_VIEW
                setData(Uri.parse(getIntent().getStringExtra("url").toString()))
            }
            startActivity(sendIntent)
        }
        binding.imageShare.setOnClickListener {

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getIntent().getStringExtra("url").toString())
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, getIntent().getStringExtra("name"))
            startActivity(shareIntent)
        }
        binding.textName.setText(getIntent().getStringExtra("name"))
        binding.webView.loadUrl(getIntent().getStringExtra("url").toString())
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.builtInZoomControls = true
        binding.webView.settings.displayZoomControls = true

    }
}