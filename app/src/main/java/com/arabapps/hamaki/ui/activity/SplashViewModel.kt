package com.arabapps.hamaki.ui.activity

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arabapps.hamaki.BuildConfig
import com.arabapps.hamaki.R
import com.arabapps.hamaki.api.RetrofitClient
import com.arabapps.hamaki.data.ErrorResponse
import com.arabapps.hamaki.data.ProfileResponse
import com.arabapps.hamaki.data.VersionResponse
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.sasco.user.helper.DialogHelper
import com.sasco.user.helper.NetworkConnectionHelper
import com.sasco.user.helper.SharedHelper
import retrofit2.Response

private const val TAG = "SplashViewModel"

class SplashViewModel : ViewModel() {


    val viewmodels: MutableLiveData<VersionResponse> = MutableLiveData()
    fun checkVersion(context: Context) {
        if (!NetworkConnectionHelper.isNetworkConnected(context))
            return
        RetrofitClient.retrofitServices.checkVersion()
            .enqueue(object : retrofit2.Callback<VersionResponse> {


                override fun onFailure(call: retrofit2.Call<VersionResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ", t)
                    DialogHelper.printMessage(
                        context,
                        context.resources.getString(R.string.something_error)
                    )
                    viewmodels.postValue(null)
                }

                override fun onResponse(
                    call: retrofit2.Call<VersionResponse>,
                    response: Response<VersionResponse>
                ) {
                    if (BuildConfig.DEBUG)  Log.d(TAG, "onResponse: " + response.code())
                    if (response.isSuccessful && response.body() != null) {
                        if (BuildConfig.DEBUG)  Log.d(TAG, "onResponse: " + response.body().toString())
                        viewmodels.value = response.body()
                    } else if (response.code() == 400) {

                        val gson = Gson()
                        val message: ErrorResponse = gson.fromJson(
                            response.errorBody()!!.charStream(),
                            ErrorResponse::class.java
                        )

                        DialogHelper.printMessage(context, message.error.toString())
                    } else if (response.code() == 401) {
                        SharedHelper.saveString(context, SharedHelper.TOKEN, "")
                        viewmodels.postValue(null)
                    } else if (response.code() == 307) {
                        val intent = Intent(context, RedirectInfoActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                    } else {
                        viewmodels.postValue(null)
                        DialogHelper.printMessage(context, response.message())
                    }
                }

            })


    }


    fun profile(context: Context) {
        if (!NetworkConnectionHelper.isNetworkConnected(context))
            return
        RetrofitClient.retrofitServices.profile(
            SharedHelper.getString(context, SharedHelper.TOKEN).toString()
        )
            .enqueue(object : retrofit2.Callback<ProfileResponse> {


                override fun onFailure(call: retrofit2.Call<ProfileResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ", t)
                    /* DialogHelper.printMessage(
                         context,
                         context.resources.getString(R.string.something_error)
                     )*/

                }

                override fun onResponse(
                    call: retrofit2.Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {
                    if (BuildConfig.DEBUG)  Log.d(TAG, "onResponse: " + response.code())
                    if (response.isSuccessful && response.body() != null) {
                        if (BuildConfig.DEBUG)  Log.d(TAG, "onResponse: " + response.body().toString())


                        SharedHelper.saveString(
                            context,
                            SharedHelper.NAME,
                            response.body()?.firstName.toString()
                        )
                        SharedHelper.saveString(
                            context,
                            SharedHelper.IMAGE,
                            response.body()?.imagePath.toString()
                        )
                        SharedHelper.saveString(
                            context,
                            SharedHelper.GROUP,
                            response.body()?.group?.name.toString()
                        )
                        SharedHelper.saveString(
                            context,
                            SharedHelper.PHONE,
                            response.body()?.email.toString()
                        )

                        FirebaseMessaging.getInstance().unsubscribeFromTopic("year-one")
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("year-two")
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("year-three")
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("year-four")

                        if (response.body()?.group?.name?.toString()?.trim()?.toLowerCase()
                                ?.contains("four")!!
                        )
                            FirebaseMessaging.getInstance().subscribeToTopic("year-four")

                        if (response.body()?.group?.name?.toString()?.trim()?.toLowerCase()
                                ?.contains("two")!!
                        )
                            FirebaseMessaging.getInstance().subscribeToTopic("year-two")

                        if (response.body()?.group?.name?.toString()?.trim()?.toLowerCase()
                                ?.contains("three")!!
                        )
                            FirebaseMessaging.getInstance().subscribeToTopic("year-three")

                        if (response.body()?.group?.name?.toString()?.trim()?.toLowerCase()
                                ?.contains("one")!!
                        )
                            FirebaseMessaging.getInstance().subscribeToTopic("year-one")



                        SharedHelper.saveInt(
                            context,
                            SharedHelper.ID,
                            response.body()!!.id
                        )

                    } else if (response.code() == 400) {

                        val gson = Gson()
                        val message: ErrorResponse = gson.fromJson(
                            response.errorBody()!!.charStream(),
                            ErrorResponse::class.java
                        )

                        DialogHelper.printMessage(context, message.error.toString())
                    } else if (response.code() == 401) {

                    } else {

                        DialogHelper.printMessage(context, response.message())
                    }
                }

            })
    }
}