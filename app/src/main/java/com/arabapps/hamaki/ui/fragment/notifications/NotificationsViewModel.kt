package com.arabapps.hamaki.ui.fragment.notifications

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arabapps.hamaki.BuildConfig
import com.arabapps.hamaki.R
import com.arabapps.hamaki.api.RetrofitClient
import com.arabapps.hamaki.data.AllNotificationsResponse
import com.arabapps.hamaki.data.ErrorResponse
import com.arabapps.hamaki.ui.activity.RedirectInfoActivity
import com.google.gson.Gson
import com.sasco.user.helper.DialogHelper
import com.sasco.user.helper.NetworkConnectionHelper
import com.sasco.user.helper.SharedHelper
import retrofit2.Response

private const val TAG = "NotificationsViewModel"
class NotificationsViewModel : ViewModel() {

    val loginLiveData: MutableLiveData<AllNotificationsResponse> = MutableLiveData()
    fun notifications(context: Context, page: Int) {
        if (!NetworkConnectionHelper.isNetworkConnected(context))
            return
        RetrofitClient.retrofitServices.notifications(
            SharedHelper.getString(context, SharedHelper.TOKEN).toString(), page
        )
            .enqueue(object : retrofit2.Callback<AllNotificationsResponse> {


                override fun onFailure(call: retrofit2.Call<AllNotificationsResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ", t)
                    DialogHelper.printMessage(
                        context,
                        context.resources.getString(R.string.something_error)
                    )
                    loginLiveData.postValue(null)
                }

                override fun onResponse(
                    call: retrofit2.Call<AllNotificationsResponse>,
                    response: Response<AllNotificationsResponse>
                ) {
                    if (BuildConfig.DEBUG)  Log.d(TAG, "onResponse: " + response.code())
                    if (response.isSuccessful && response.body() != null) {
                        if (BuildConfig.DEBUG)  Log.d(TAG, "onResponse: " + response.body().toString())
                        loginLiveData.value = response.body()
                    } else if (response.code() == 400) {

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


}