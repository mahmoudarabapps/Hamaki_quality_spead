package com.sasco.user.helper

import android.content.Context
import android.content.SharedPreferences

class SharedHelper {


    companion object {
        val FULL_SCREEN: String = "isFullscreen"
        val TRIP_STATUS: String = "_tripStatus"
        val TRIP_STARTED: String = "_isTripStarted"
        var sharedPreferences: SharedPreferences? = null

        val NAME: String = "_name"
        val GROUP="_group"
        val PHONE: String = "_phone"
        val ID: String = "_ID"
        val IMAGE: String = "_image"
        val TOKEN: String = "_token"


        private fun getSharedPref(context: Context): SharedPreferences? {
            if (sharedPreferences == null) {
                sharedPreferences = context.getSharedPreferences("app_data", Context.MODE_PRIVATE)
            }
            return sharedPreferences
        }

        private fun getSharedPrefEditor(context: Context): SharedPreferences.Editor? {
            return getSharedPref(context)?.edit()
        }

        public fun clearAll(context: Context) {
            getSharedPrefEditor(context)?.clear()?.apply();
        }

        public fun saveString(context: Context, key: String, value: String) {
            getSharedPrefEditor(context)?.putString(key, value)?.apply()
        }

        public fun saveInt(context: Context, key: String, value: Int) {
            getSharedPrefEditor(context)?.putInt(key, value)?.apply()
        }

        public fun saveBoolean(context: Context, key: String, value: Boolean) {
            getSharedPrefEditor(context)?.putBoolean(key, value)?.apply()
        }

        public fun getString(context: Context, key: String): String? {
            return getSharedPref(context)?.getString(key, "")
        }

        public fun getBoolean(context: Context, key: String): Boolean? {
            return getSharedPref(context)?.getBoolean(key, false)
        }

        public fun getInt(context: Context, key: String): Int? {
            return getSharedPref(context)?.getInt(key, -1)
        }


        /*  public fun saveUser(context: Context, user: User) {
              saveString(context, TOKEN, user.apiToken.toString())
              saveString(context, NAME, user.name.toString())
              saveString(context, PHONE, user.mobile.toString())
              saveString(context, EMAIL, user.email.toString())
              saveInt(context, ID, user?.id!!)
              saveString(context, IMAGE, user.image.toString())
          }*/
    }
}