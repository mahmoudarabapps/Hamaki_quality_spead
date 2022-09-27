package com.arabapps.hamaki.ui.activity.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arabapps.hamaki.BuildConfig
import com.arabapps.hamaki.R
import com.arabapps.hamaki.api.RetrofitClient
import com.arabapps.hamaki.data.ErrorResponse
import com.arabapps.hamaki.data.LoginTokenResponse
import com.arabapps.hamaki.ui.activity.RedirectInfoActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.sasco.user.helper.DialogHelper
import com.sasco.user.helper.NetworkConnectionHelper
import com.sasco.user.helper.SharedHelper
import retrofit2.Response


private const val TAG = "LoginViewModel"

class LoginViewModel : ViewModel() {

    val loginLiveData: MutableLiveData<LoginTokenResponse> = MutableLiveData()

    fun login(context: Context, mobile: String, password: String, device_id: String) {
        if (!NetworkConnectionHelper.isNetworkConnected(context))
            return
        if (BuildConfig.DEBUG)  Log.d(TAG, "login: " + mobile + "\t" + password)
        RetrofitClient.retrofitServices.login(mobile, password, device_id)
            .enqueue(object : retrofit2.Callback<LoginTokenResponse> {


                override fun onFailure(call: retrofit2.Call<LoginTokenResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ", t)
                    DialogHelper.printMessage(
                        context,
                        context.resources.getString(R.string.something_error)
                    )
                    loginLiveData.postValue(null)
                }

                override fun onResponse(
                    call: retrofit2.Call<LoginTokenResponse>,
                    response: Response<LoginTokenResponse>
                ) {
                    if (BuildConfig.DEBUG)  Log.d(TAG, "onResponse: " + response.code())
                    if (response.isSuccessful && response.body() != null) {
                        SharedHelper.saveString(
                            context,
                            SharedHelper.TOKEN,
                            "Bearer " + response.body()?.token.toString()
                        )
                        SharedHelper.saveString(
                            context,
                            SharedHelper.NAME,
                            response.body()?.student?.firstName.toString()
                        )
                        SharedHelper.saveString(
                            context,
                            SharedHelper.IMAGE,
                            response.body()?.student?.imagePath.toString()
                        )
                        SharedHelper.saveString(
                            context,
                            SharedHelper.GROUP,
                            response.body()?.student?.group?.name.toString()
                        )
                        SharedHelper.saveString(
                            context,
                            SharedHelper.PHONE,
                            response.body()?.student?.email.toString()
                        )
                        response.body()!!.student?.id?.let {
                            SharedHelper.saveInt(
                                context,
                                SharedHelper.ID,
                                it
                            )
                        }
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("year-one")
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("year-two")
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("year-three")
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("year-four")

                        if (response.body()?.student?.group?.name?.toString()?.trim()?.toLowerCase()
                                ?.contains("four")!!
                        )
                            FirebaseMessaging.getInstance().subscribeToTopic("year-four")

                        if (response.body()?.student?.group?.name?.toString()?.trim()?.toLowerCase()
                                ?.contains("two")!!
                        )
                            FirebaseMessaging.getInstance().subscribeToTopic("year-two")

                        if (response.body()?.student?.group?.name?.toString()?.trim()?.toLowerCase()
                                ?.contains("three")!!
                        )
                            FirebaseMessaging.getInstance().subscribeToTopic("year-three")

                        if (response.body()?.student?.group?.name?.toString()?.trim()?.toLowerCase()
                                ?.contains("one")!!
                        )
                            FirebaseMessaging.getInstance().subscribeToTopic("year-one")



                        loginLiveData.value = response.body()
                        if (BuildConfig.DEBUG)  Log.d(TAG, "onResponse: " + response.body().toString())

                    } else if (response.code() == 400) {
                        loginLiveData.postValue(null)
                        val gson = Gson()
                        val message: ErrorResponse = gson.fromJson(
                            response.errorBody()!!.charStream(),
                            ErrorResponse::class.java
                        )

                        DialogHelper.printMessage(context, message.error.toString())
                    } else if (response.code() == 307) {
                        val intent=Intent(context,RedirectInfoActivity::class.java)
                        intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)

                    } else {
                        loginLiveData.postValue(null)
                        DialogHelper.printMessage(context, response.message())
                    }
                }

            })


    }
}