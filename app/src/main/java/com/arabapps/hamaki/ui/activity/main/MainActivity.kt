package com.arabapps.hamaki.ui.activity.main

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.arabapps.hamaki.R
import com.arabapps.hamaki.databinding.ActivityMainBinding
import com.fxn.OnBubbleClickListener


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