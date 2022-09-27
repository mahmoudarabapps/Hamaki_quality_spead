package com.arabapps.hamaki.helper

import android.util.Log
import com.arabapps.hamaki.BuildConfig
import java.text.SimpleDateFormat
import java.util.*
private const val TAG = "TimeHelper"

class TimeHelper {
    companion object {
        private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US)

        init {
            simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT")
            dateFormat.timeZone = TimeZone.getDefault()
        }

        fun formatTime(createdAt: String): String {
            if (BuildConfig.DEBUG)  Log.d(TAG, "formatTime: $createdAt")
            try {
                return dateFormat.format(simpleDateFormat.parse(createdAt))
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return createdAt
        }
    }
}