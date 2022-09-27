package com.arabapps.hamaki.ui.fragment.account

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arabapps.hamaki.BuildConfig
import com.arabapps.hamaki.R
import com.arabapps.hamaki.api.RequestbodyUtils
import com.arabapps.hamaki.api.RetrofitClient
import com.arabapps.hamaki.data.ErrorResponse
import com.arabapps.hamaki.data.ProfileResponse
import com.arabapps.hamaki.data.UpdateProfileResponse
import com.arabapps.hamaki.ui.activity.RedirectInfoActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.sasco.user.helper.DialogHelper
import com.sasco.user.helper.NetworkConnectionHelper
import com.sasco.user.helper.SharedHelper
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

private const val TAG = "AccountViewModel"

class AccountViewModel : ViewModel() {


    val loginLiveData: MutableLiveData<ProfileResponse> = MutableLiveData()

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
                    loginLiveData.postValue(null)
                }

                override fun onResponse(
                    call: retrofit2.Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {
                    if (BuildConfig.DEBUG)  Log.d(TAG, "onResponse: " + response.code())
                    if (response.isSuccessful && response.body() != null) {
                        if (BuildConfig.DEBUG)  Log.d(TAG, "onResponse: " + response.body().toString())
                        loginLiveData.value = response.body()

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
                        loginLiveData.postValue(null)
                        val gson = Gson()
                        val message: ErrorResponse = gson.fromJson(
                            response.errorBody()!!.charStream(),
                            ErrorResponse::class.java
                        )

                        DialogHelper.printMessage(context, message.error.toString())
                    } else if (response.code() == 401) {
                        SharedHelper.saveString(context, SharedHelper.TOKEN, "")
                        loginLiveData.postValue(null)
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


////////////////////////////////


    val updateLiveData: MutableLiveData<UpdateProfileResponse> = MutableLiveData()

    fun updateProfile(context: Context, password: String, image: RequestBody?) {
        if (!NetworkConnectionHelper.isNetworkConnected(context))
            return
        //  @Part("password") password: RequestBody,
        //        @Part image: MultipartBody.Part
        if (BuildConfig.DEBUG)  Log.d(
            TAG,
            "updateProfile: " + SharedHelper.getString(context, SharedHelper.TOKEN)
                .toString() + " " + password
        )
        var request: Call<UpdateProfileResponse>? = null
        if (image != null)
            request = RetrofitClient.retrofitServices.update_profile(
                SharedHelper.getString(context, SharedHelper.TOKEN).toString(),
                RequestbodyUtils.convertToRequestBody(password),
                RequestbodyUtils.convertToRequestBodyPart("image", image)
            )
        else
            request = RetrofitClient.retrofitServices.update_profile(
                SharedHelper.getString(context, SharedHelper.TOKEN).toString(),
                (password)
            )
        request.enqueue(object : retrofit2.Callback<UpdateProfileResponse> {


            override fun onFailure(call: retrofit2.Call<UpdateProfileResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ", t)
                DialogHelper.printMessage(
                    context,
                    context.resources.getString(R.string.something_error)
                )
                updateLiveData.postValue(null)
            }

            override fun onResponse(
                call: retrofit2.Call<UpdateProfileResponse>,
                response: Response<UpdateProfileResponse>
            ) {
                if (BuildConfig.DEBUG)  Log.d(TAG, "onResponse: " + response.code())
                if (response.isSuccessful && response.body() != null) {
                    if (BuildConfig.DEBUG)  Log.d(TAG, "onResponse: " + response.body().toString())
                    updateLiveData.value = response.body()
                } else if (response.code() == 400) {
                    updateLiveData.postValue(null)
                    val gson = Gson()
                    val message: ErrorResponse = gson.fromJson(
                        response.errorBody()!!.charStream(),
                        ErrorResponse::class.java
                    )

                    DialogHelper.printMessage(context, message.error.toString())
                } else if (response.code() == 422) {
                    updateLiveData.postValue(null)

                    DialogHelper.printMessage(
                        context,
                        context.resources.getString(R.string.password_weak)
                    )
                } else if (response.code() == 401) {
                    SharedHelper.saveString(context, SharedHelper.TOKEN, "")
                    updateLiveData.postValue(null)
                } else if (response.code() == 307) {
                   val intent=Intent(context,RedirectInfoActivity::class.java)
                        intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                } else {
                    updateLiveData.postValue(null)
                    DialogHelper.printMessage(context, response.message())
                }
            }

        })


    }


}