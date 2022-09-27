package com.arabapps.hamaki.ui.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.arabapps.hamaki.BuildConfig
import com.arabapps.hamaki.databinding.ActivityFullScreenVideoBinding
import com.sasco.user.helper.SharedHelper
import kotlin.random.Random

private const val TAG = "FullScreenVideoActivity"

class FullScreenVideoActivity : AppCompatActivity() {
    var isHeadset = -1
    val br: BroadcastReceiver = broadcastReceiver()

    inner class broadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent!!.action
            var iii = 2
            if (Intent.ACTION_HEADSET_PLUG == action) {
                iii = intent.getIntExtra("state", -1)
                if (Integer.valueOf(iii) == 0) {
                    isHeadset = 0
                    binding.root.context.let {
                        binding.toastLayout.visibility = View.VISIBLE

                        binding.videoView.onPause()
                        binding.pauseView.visibility = View.VISIBLE
                    }
                }
                if (Integer.valueOf(iii) == 1) {
                    isHeadset = 1
                    binding.pauseView.visibility = View.GONE
                    binding.videoView.onResume()
                    binding.toastLayout.visibility = View.GONE
                }
            }
        }

    }

    private lateinit var binding: ActivityFullScreenVideoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        binding = ActivityFullScreenVideoBinding.inflate(layoutInflater)

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(binding.root)
        val receiverFilter = IntentFilter(Intent.ACTION_HEADSET_PLUG)
        binding.root.context.registerReceiver(br, receiverFilter)
        binding.tempMessage.text =
            SharedHelper.getString(binding.root.context, SharedHelper.NAME) + "\n" +
                    SharedHelper.getString(binding.root.context, SharedHelper.PHONE)
        intent.getStringExtra("url")?.let { binding.videoView.loadUrl("https://player.vimeo.com/video/$it") }
        animateText()


    }
    private val handler = Handler(Looper.myLooper()!!)

    private fun animateText() {
        try {

            if (binding.tempMessage.visibility != View.GONE) {
                handler.postDelayed(Runnable {
                    binding.tempMessage.visibility = View.GONE
                    animateText()
                }, 3000)
            } else
                handler.postDelayed(Runnable {
                    val x = binding.videoView.width - binding.tempMessage.width
                    val y = binding.videoView.height - binding.tempMessage.height
                    val xRandom = Random.nextInt(Math.abs(x))
                    val yRandom = Random.nextInt(Math.abs(y))
                    if (BuildConfig.DEBUG)  Log.d(TAG, "aniumateText: ")
                    try {

//

                        runOnUiThread {
                            binding.tempMessage.x = xRandom.toFloat()
                            binding.tempMessage.y = yRandom.toFloat()
                            binding.tempMessage.visibility = ViewGroup.VISIBLE
                        }
                    }catch (exxx:java.lang.Exception){

                    }
                    binding.tempMessage.visibility = View.VISIBLE
                    animateText()
                }, 7000)
        } catch (ex: java.lang.Exception) {
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (BuildConfig.DEBUG)  Log.d(TAG, "onDestroy: ")
        try {
            unregisterReceiver(br)

        } catch (ex: Exception) {
        }


    }
}