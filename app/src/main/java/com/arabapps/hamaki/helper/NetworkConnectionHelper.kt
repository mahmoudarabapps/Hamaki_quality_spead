package com.sasco.user.helper

import android.content.Context
import android.net.ConnectivityManager
import com.arabapps.hamaki.R

class NetworkConnectionHelper {
    companion object {
        fun isNetworkConnected(context: Context): Boolean {
            val cm = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            if (!(netInfo != null && netInfo.isConnectedOrConnecting))
                DialogHelper.printMessage(
                    context,
                    context.resources.getString(R.string.no_internet_connection)
                )
            return netInfo != null && netInfo.isConnectedOrConnecting
        }
    }
}