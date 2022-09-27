package com.arabapps.hamaki.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitClient {
    companion object {
        var gson: Gson? = GsonBuilder()
            .setLenient()
            .create()


        var httpClient = OkHttpClient.Builder()
            .callTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .followRedirects(false)
            .followSslRedirects(false)
            .writeTimeout(30, TimeUnit.SECONDS)

//        val retrofitServices = Retrofit.Builder().baseUrl("https://amr-hamaki.com/admin/api/")
        

//        val retrofitServices = Retrofit.Builder().baseUrl("http://hamaki.arabappsdemo.com/api/")
        val retrofitServices = Retrofit.Builder().baseUrl("https://dashbord.amr-hamaki.com/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build().create(RetrofitServices::class.java)

    }
}