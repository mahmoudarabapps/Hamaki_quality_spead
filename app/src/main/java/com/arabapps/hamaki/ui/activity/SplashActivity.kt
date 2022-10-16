package com.arabapps.hamaki.ui.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.arabapps.hamaki.BuildConfig
import com.arabapps.hamaki.databinding.ActivitySplashBinding
import com.arabapps.hamaki.ui.activity.login.LoginActivity
import com.arabapps.hamaki.ui.activity.main.MainActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.sasco.user.helper.SharedHelper
import java.io.File


private const val TAG = "SplashActivity"

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.profile(this)
        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().subscribeToTopic("hamaki-app")
        binding.imageView2.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(1000)
            .setStartDelay(1000)
            .startDelay

        /*if (isProbablyRunningOnEmulator()) {

            binding.card.visibility = View.VISIBLE
            binding.button.text = "Ok"
            binding.message.text = "Emulator not allowed , please use a real device "
            binding.button.setOnClickListener {
                finishAffinity()
            }

            return
        }
*/
//
        viewModel.checkVersion(this)
        viewModel.viewmodels.observe(this, Observer {
            if (it != null) {
                try {
                    val pInfo: PackageInfo =
                        packageManager.getPackageInfo(packageName, 0)
                    val version: String = pInfo.versionName
                    if (it.version_status.equals("0") && !isProbablyRunningOnEmulator()) {
                        Handler(Looper.myLooper()!!).postDelayed({
                            var intent: Intent? = null
                            if (SharedHelper.getString(this@SplashActivity, SharedHelper.TOKEN)
                                    .isNullOrEmpty()
                            )
                                intent = Intent(applicationContext, LoginActivity::class.java)
                            else intent = Intent(applicationContext, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                            finish()
                        }, 1000)
                    } else if (version.trim()
                            .equals(it.android_version?.trim()) && !isProbablyRunningOnEmulator()
                    ) {
                        Handler(Looper.myLooper()!!).postDelayed({

                            var intent: Intent? = null
                            if (SharedHelper.getString(this@SplashActivity, SharedHelper.TOKEN)
                                    .isNullOrEmpty()
                            )
                                intent = Intent(applicationContext, LoginActivity::class.java)
                            else intent = Intent(applicationContext, MainActivity::class.java)
                            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                            finish()
                        }, 1000)
                    } else if (!isProbablyRunningOnEmulator()) {
                        binding.card.visibility = View.VISIBLE
                        binding.button.text = "Update"
                        binding.message.text = "Current version not supported now, update now "
                        binding.button.setOnClickListener {
                            val i: Intent = Intent(android.content.Intent.ACTION_VIEW);
                            i.setData(Uri.parse("https://play.google.com/store/apps/details?id=$packageName"));
                            startActivity(i);
                            finishAffinity()
                        }

                    } else {
                        binding.card.visibility = View.VISIBLE
                        binding.button.text = "Ok"
                        binding.message.text = "Emulator not allowed , please use a real device "
                        binding.button.setOnClickListener {
                            finishAffinity()
                        }
                    }
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }
            } else {

                binding.card.visibility = View.VISIBLE

                binding.message.text = "Something error"
                binding.button.text = "try again"
                binding.button.setOnClickListener {
                    viewModel.checkVersion(context = applicationContext)
                }


            }
        })
    }


    fun isProbablyRunningOnEmulator(): Boolean {
        if (BuildConfig.DEBUG) {
            return false
        }
        // Android SDK emulator
        val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val networkOperator = tm.networkOperatorName

        if ("Android" == networkOperator || "02 - uk".trim()
                .equals(networkOperator.trim().toLowerCase()) || "02-uk".trim()
                .equals(networkOperator.trim().toLowerCase()) || "02-UK".trim()
                .equals(networkOperator.trim().toLowerCase()) || "02 - UK".trim()
                .equals(networkOperator.trim().toLowerCase()) ||
            networkOperator.trim().toLowerCase().contains("uk", true)
        ) {
            return true
        }
        return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator")

    }

    fun onBlueStacks(): Boolean? {
        val sharedFolder = File(
            (Environment
                .getExternalStorageDirectory().toString()
                    + File.separatorChar
                    ).toString() + "windows"
                    + File.separatorChar
                .toString() + "BstSharedFolder"
        )
        return sharedFolder.exists()
    }
}