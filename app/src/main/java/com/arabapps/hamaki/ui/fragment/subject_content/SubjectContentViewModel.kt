package com.arabapps.hamaki.ui.fragment.subject_content

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
import com.arabapps.hamaki.data.MessageResponse
import com.arabapps.hamaki.data.SubjectWithIDResponse
import com.arabapps.hamaki.ui.activity.RedirectInfoActivity
import com.google.gson.Gson
import com.sasco.user.helper.DialogHelper
import com.sasco.user.helper.NetworkConnectionHelper
import com.sasco.user.helper.SharedHelper
import retrofit2.Response

private const val TAG = "SubjectContentViewModel"

class SubjectContentViewModel : ViewModel() {


    val loginLiveData: MutableLiveData<SubjectWithIDResponse> = MutableLiveData()

    fun subjectByID(context: Context, id: Int) {
        if (!NetworkConnectionHelper.isNetworkConnected(context))
            return
        RetrofitClient.retrofitServices.subject(
            SharedHelper.getString(context, SharedHelper.TOKEN).toString(), id
        )
            .enqueue(object : retrofit2.Callback<SubjectWithIDResponse> {


                override fun onFailure(call: retrofit2.Call<SubjectWithIDResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ", t)
                    DialogHelper.printMessage(
                        context,
                        context.resources.getString(R.string.something_error)
                    )
                    loginLiveData.postValue(null)
                }

                override fun onResponse(
                    call: retrofit2.Call<SubjectWithIDResponse>,
                    response: Response<SubjectWithIDResponse>
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


    fun addBookmark(context: Context, id: Int, lectID: Int)  :MutableLiveData<MessageResponse>{
        val livdata=MutableLiveData<MessageResponse>()
        if (!NetworkConnectionHelper.isNetworkConnected(context))
            return livdata
        RetrofitClient.retrofitServices.bookmarks(
            SharedHelper.getString(context, SharedHelper.TOKEN).toString(), id
        )
            .enqueue(object : retrofit2.Callback<MessageResponse> {


                override fun onFailure(call: retrofit2.Call<MessageResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ", t)
                    DialogHelper.printMessage(
                        context,
                        context.resources.getString(R.string.something_error)
                    )

                }

                override fun onResponse(
                    call: retrofit2.Call<MessageResponse>,
                    response: Response<MessageResponse>
                ) {
                    if (BuildConfig.DEBUG)  Log.d(TAG, "onResponse: " + response.code())
                    if (response.isSuccessful && response.body() != null) {
                        if (BuildConfig.DEBUG)  Log.d(TAG, "onResponse: " + response.body().toString())
                        subjectByID(context, lectID)
                    } else if (response.code() == 400) {

                        val gson = Gson()
                        val message: ErrorResponse = gson.fromJson(
                            response.errorBody()!!.charStream(),
                            ErrorResponse::class.java
                        )

                        DialogHelper.printMessage(context, message.error.toString())
                    } else if (response.code() == 401) {
                        SharedHelper.saveString(context, SharedHelper.TOKEN, "")

                    } else {

                        DialogHelper.printMessage(context, response.message())
                    }
                }

            })
return livdata

    }

    fun deleteBookmarks(context: Context, id: Int, lectID: Int)  :MutableLiveData<MessageResponse>{
        val livdata=MutableLiveData<MessageResponse>()
        if (!NetworkConnectionHelper.isNetworkConnected(context))
            return livdata
        RetrofitClient.retrofitServices.bookmarksDelete(
            SharedHelper.getString(context, SharedHelper.TOKEN).toString(), id
        )
            .enqueue(object : retrofit2.Callback<MessageResponse> {


                override fun onFailure(call: retrofit2.Call<MessageResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ", t)
                    DialogHelper.printMessage(
                        context,
                        context.resources.getString(R.string.something_error)
                    )

                }

                override fun onResponse(
                    call: retrofit2.Call<MessageResponse>,
                    response: Response<MessageResponse>
                ) {
                    if (BuildConfig.DEBUG)  Log.d(TAG, "onResponse: " + response.code())
                    if (response.isSuccessful && response.body() != null) {
                        if (BuildConfig.DEBUG)  Log.d(TAG, "onResponse: " + response.body().toString())
                        subjectByID(context, lectID)
                    } else if (response.code() == 400) {

                        val gson = Gson()
                        val message: ErrorResponse = gson.fromJson(
                            response.errorBody()!!.charStream(),
                            ErrorResponse::class.java
                        )

                        DialogHelper.printMessage(context, message.error.toString())
                    } else if (response.code() == 401) {
                        SharedHelper.saveString(context, SharedHelper.TOKEN, "")

                    } else if (response.code() == 307) {
                       val intent=Intent(context,RedirectInfoActivity::class.java)
                        intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                    } else {

                        DialogHelper.printMessage(context, response.message())
                    }
                }

            })

return livdata
    }
}