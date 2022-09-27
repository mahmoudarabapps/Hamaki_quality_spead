package com.arabapps.hamaki.ui.fragment.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arabapps.hamaki.BuildConfig
import com.arabapps.hamaki.R
import com.arabapps.hamaki.api.RetrofitClient
import com.arabapps.hamaki.data.*
import com.arabapps.hamaki.ui.activity.RedirectInfoActivity
import com.google.gson.Gson
import com.sasco.user.helper.DialogHelper
import com.sasco.user.helper.NetworkConnectionHelper
import com.sasco.user.helper.SharedHelper
import retrofit2.Response

private const val TAG = "HomeViewModel"

class HomeViewModel : ViewModel() {

    val loginLiveData: MutableLiveData<PostsResponse> = MutableLiveData()
    fun post(context: Context) {
        if (!NetworkConnectionHelper.isNetworkConnected(context))
            return
        RetrofitClient.retrofitServices.posts(
            SharedHelper.getString(context, SharedHelper.TOKEN).toString(), 40, "desc", "id"
        )
            .enqueue(object : retrofit2.Callback<PostsResponse> {
                override fun onFailure(call: retrofit2.Call<PostsResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ", t)
                    DialogHelper.printMessage(
                        context,
                        context.resources.getString(R.string.something_error)
                    )
                    loginLiveData.postValue(null)
                }

                override fun onResponse(
                    call: retrofit2.Call<PostsResponse>,
                    response: Response<PostsResponse>
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


    val materialsViemodel: MutableLiveData<MyGroupsResponse> = MutableLiveData()

    fun materials(context: Context) {
        if (!NetworkConnectionHelper.isNetworkConnected(context))
            return
        RetrofitClient.retrofitServices.group(
            SharedHelper.getString(context, SharedHelper.TOKEN).toString()
        )
            .enqueue(object : retrofit2.Callback<MyGroupsResponse> {


                override fun onFailure(call: retrofit2.Call<MyGroupsResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ", t)
                    DialogHelper.printMessage(
                        context,
                        context.resources.getString(R.string.something_error)
                    )
                    materialsViemodel.postValue(null)
                }

                override fun onResponse(
                    call: retrofit2.Call<MyGroupsResponse>,
                    response: Response<MyGroupsResponse>
                ) {
                    if (BuildConfig.DEBUG)  Log.d(TAG, "onResponse: " + response.code())
                    if (response.isSuccessful && response.body() != null) {
                        if (BuildConfig.DEBUG)  Log.d(TAG, "onResponse: " + response.body().toString())
                        materialsViemodel.value = response.body()
                    } else if (response.code() == 400) {

                        val gson = Gson()
                        val message: ErrorResponse = gson.fromJson(
                            response.errorBody()!!.charStream(),
                            ErrorResponse::class.java
                        )

                        DialogHelper.printMessage(context, message.error.toString())
                    } else if (response.code() == 401) {
                        SharedHelper.saveString(context, SharedHelper.TOKEN, "")
                        materialsViemodel.postValue(null)
                    } else if (response.code() == 307) {
                       val intent=Intent(context,RedirectInfoActivity::class.java)
                        intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                    } else {
                        materialsViemodel.postValue(null)
                        DialogHelper.printMessage(context, response.message())
                    }
                }

            })


    }


    val lastLecturesLivedata: MutableLiveData<List<LastLectureResponse>> = MutableLiveData()

    fun lectureLatest(context: Context) {
        if (!NetworkConnectionHelper.isNetworkConnected(context))
            return
        RetrofitClient.retrofitServices.lectureLatest(
            SharedHelper.getString(context, SharedHelper.TOKEN).toString()
        )
            .enqueue(object : retrofit2.Callback<List<LastLectureResponse>> {


                override fun onFailure(
                    call: retrofit2.Call<List<LastLectureResponse>>,
                    t: Throwable
                ) {
                    Log.e(TAG, "onFailure: ", t)
                    DialogHelper.printMessage(
                        context,
                        context.resources.getString(R.string.something_error)
                    )
                    lastLecturesLivedata.postValue(null)
                }

                override fun onResponse(
                    call: retrofit2.Call<List<LastLectureResponse>>,
                    response: Response<List<LastLectureResponse>>
                ) {
                    if (BuildConfig.DEBUG)  Log.d(TAG, "onResponse: " + response.code())
                    if (response.isSuccessful && response.body() != null) {
                        if (BuildConfig.DEBUG)  Log.d(TAG, "onResponse: " + response.body().toString())
                        lastLecturesLivedata.value = response.body()
                    } else if (response.code() == 400) {

                        val gson = Gson()
                        val message: ErrorResponse = gson.fromJson(
                            response.errorBody()!!.charStream(),
                            ErrorResponse::class.java
                        )

                        DialogHelper.printMessage(context, message.error.toString())
                    } else if (response.code() == 401) {
                        SharedHelper.saveString(context, SharedHelper.TOKEN, "")
                        lastLecturesLivedata.postValue(null)
                    } else if (response.code() == 307) {
                       val intent=Intent(context,RedirectInfoActivity::class.java)
                        intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                    } else {
                        lastLecturesLivedata.postValue(null)
                        DialogHelper.printMessage(context, response.message())
                    }
                }

            })

    }


    /////////////////////////////
    fun addReaction(
        context: Context,
        postID: Int,
        reaction: Int
    ): MutableLiveData<MessageResponse> {
        val reactionViewModel: MutableLiveData<MessageResponse> = MutableLiveData()
        if (!NetworkConnectionHelper.isNetworkConnected(context))
            return reactionViewModel;
        RetrofitClient.retrofitServices.postReaction(
            SharedHelper.getString(context, SharedHelper.TOKEN).toString(), postID, reaction
        )
            .enqueue(object : retrofit2.Callback<MessageResponse> {
                override fun onFailure(call: retrofit2.Call<MessageResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ", t)
                    DialogHelper.printMessage(
                        context,
                        context.resources.getString(R.string.something_error)
                    )
                    reactionViewModel.postValue(null)
                }

                override fun onResponse(
                    call: retrofit2.Call<MessageResponse>,
                    response: Response<MessageResponse>
                ) {
                    if (BuildConfig.DEBUG)  Log.d(TAG, "onResponse: " + response.code())
                    if (response.isSuccessful && response.body() != null) {
                        if (BuildConfig.DEBUG)  Log.d(TAG, "onResponse: " + response.body().toString())
                        reactionViewModel.value = response.body()
                        post(context)
                    } else if (response.code() == 400) {

                        val gson = Gson()
                        val message: ErrorResponse = gson.fromJson(
                            response.errorBody()!!.charStream(),
                            ErrorResponse::class.java
                        )
                        DialogHelper.printMessage(context, message.error.toString())
                    } else if (response.code() == 401) {
                        SharedHelper.saveString(context, SharedHelper.TOKEN, "")
                        reactionViewModel.postValue(null)
                    } else if (response.code() == 307) {
                       val intent=Intent(context,RedirectInfoActivity::class.java)
                        intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                    } else {
                        reactionViewModel.postValue(null)
                        DialogHelper.printMessage(context, response.message())
                    }
                }

            })
        return reactionViewModel
    }


    fun deleteReaction(context: Context, postID: Int): MutableLiveData<MessageResponse> {
        val deleteReactionViewModel: MutableLiveData<MessageResponse> = MutableLiveData()

        if (!NetworkConnectionHelper.isNetworkConnected(context))
            return deleteReactionViewModel
        RetrofitClient.retrofitServices.deleteReaction(
            SharedHelper.getString(context, SharedHelper.TOKEN).toString(), postID
        )
            .enqueue(object : retrofit2.Callback<MessageResponse> {
                override fun onFailure(call: retrofit2.Call<MessageResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ", t)
                    DialogHelper.printMessage(
                        context,
                        context.resources.getString(R.string.something_error)
                    )
                    deleteReactionViewModel.postValue(null)
                }

                override fun onResponse(
                    call: retrofit2.Call<MessageResponse>,
                    response: Response<MessageResponse>
                ) {
                    if (BuildConfig.DEBUG)  Log.d(TAG, "onResponse: " + response.code())
                    if (response.isSuccessful && response.body() != null) {
                        if (BuildConfig.DEBUG)  Log.d(TAG, "onResponse: " + response.body().toString())
                        deleteReactionViewModel.value = response.body()
                        post(context)
                    } else if (response.code() == 400) {

                        val gson = Gson()
                        val message: ErrorResponse = gson.fromJson(
                            response.errorBody()!!.charStream(),
                            ErrorResponse::class.java
                        )
                        DialogHelper.printMessage(context, message.error.toString())
                    } else if (response.code() == 401) {
                        SharedHelper.saveString(context, SharedHelper.TOKEN, "")
                        deleteReactionViewModel.postValue(null)
                    } else if (response.code() == 307) {
                       val intent=Intent(context,RedirectInfoActivity::class.java)
                        intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                    } else {
                        deleteReactionViewModel.postValue(null)
                        DialogHelper.printMessage(context, response.message())
                    }
                }

            })
        return deleteReactionViewModel
    }
}