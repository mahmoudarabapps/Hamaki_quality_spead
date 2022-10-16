package com.arabapps.hamaki.ui.activity.main

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.hardware.display.DisplayManager
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.arabapps.hamaki.R
import com.arabapps.hamaki.databinding.ActivityMainBinding
import com.fxn.OnBubbleClickListener
import com.sasco.user.helper.SharedHelper


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        navController = findNavController(R.id.nav_host_fragment)
        binding.navView.setSelectedWithId(R.id.navigation_account, false)
        binding.navView.setSelectedWithId(R.id.navigation_home, false)
        binding.navView.addBubbleListener(object : OnBubbleClickListener {
            override fun onBubbleClick(id: Int) {
                navController.navigate(id)
            }
        })
        navController.addOnDestinationChangedListener { _, destination, _ ->
            try {
                binding.navView.setSelectedWithId(destination.id, false)
            } catch (ex: Exception) {
            }
        }
        //disable audio record
        val listener = object : DisplayManager.DisplayListener {
            override fun onDisplayChanged(displayId: Int) {

            }

            override fun onDisplayAdded(displayId: Int) {
                // binding.videoView?.player?.playWhenReady = false
                // binding.videoView.player?.stop()
                Log.d(TAG, "onDisplayAdded: ")
                SharedHelper.saveString(this@MainActivity, "ScreenRecord", "true")

            }

            override fun onDisplayRemoved(displayId: Int) {
                Log.d(TAG, "onDisplayRemoved: ")
                SharedHelper.saveString(this@MainActivity, "ScreenRecord", "false")
            }
        }

        val displayManager = getSystemService(Context.DISPLAY_SERVICE) as? DisplayManager
        displayManager?.registerDisplayListener(listener, null)
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            audioManager.activeRecordingConfigurations
            var mode = audioManager.mode
            Log.d(TAG, "onCreate: Smode ${audioManager.activeRecordingConfigurations.size}")
            if (audioManager.activeRecordingConfigurations.size > 0) {
                SharedHelper.saveString(this@MainActivity, "ScreenRecord", "true")
            } else {
                SharedHelper.saveString(this@MainActivity, "ScreenRecord", "false")

            }
        }

    }


    var doubleBackToExitPressedOnce = false

    override fun onBackPressed() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            return
        }
        if (navController.currentDestination?.id == R.id.navigation_home && doubleBackToExitPressedOnce) {
            finish()
            return
        } else if (navController.currentDestination?.id != R.id.navigation_home) {

            super.onBackPressed()
            return
        }

        doubleBackToExitPressedOnce = true
        Toast.makeText(this, getString(R.string.double_tab_exit), Toast.LENGTH_SHORT).show()

        Handler(Looper.myLooper()!!).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Checks the orientation of the screen
        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
            binding.navView.visibility = View.GONE
            sendBroadcast(Intent("action_landscape"))
        } else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
            binding.navView.visibility = View.VISIBLE
            sendBroadcast(Intent("action_landscape"))

        }
    }
}