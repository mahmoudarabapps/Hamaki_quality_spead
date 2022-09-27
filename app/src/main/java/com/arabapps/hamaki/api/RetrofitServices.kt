package com.arabapps.hamaki.api

import com.arabapps.hamaki.data.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitServices {

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") mobile: String,
        @Field("password") password: String,
        @Field("device_id") device_id: String
    ): Call<LoginTokenResponse>


    @GET("profile")
    fun profile(@Header("Authorization") api_token: String): Call<ProfileResponse>

    @Multipart
    @POST("update_profile")
    fun update_profile(
        @Header("Authorization") api_token: String,
        @Part("password") password: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Call<UpdateProfileResponse>

    @FormUrlEncoded
    @POST("update_profile")
    fun update_profile(
        @Header("Authorization") api_token: String,
        @Field("password") password: String?
    ): Call<UpdateProfileResponse>

    @GET("group")
    fun group(@Header("Authorization") api_token: String): Call<MyGroupsResponse>


    @GET("subject/{id}")
    fun subject(
        @Header("Authorization") api_token: String,
        @Path("id") id: Int
    ): Call<SubjectWithIDResponse>


    @GET("lecture/{id}")
    fun lecture(
        @Header("Authorization") api_token: String,
        @Path("id") id: Int
    ): Call<LectureByIDResponse>

    @GET("posts")
    fun posts(
        @Header("Authorization") api_token: String, @Query("per_page") page: Int,
        @Query("sort") sort: String, @Query("sort_by") sort_by: String
    ): Call<PostsResponse>

    @GET("lecture_latest")
    fun lectureLatest(@Header("Authorization") api_token: String): Call<List<LastLectureResponse>>

    @GET("all_bookmarks")
    fun allBookmarks(
        @Header("Authorization") api_token: String,
        @Query("page") page: Int
    ): Call<AllBookmarksResponse>

    @POST("bookmarks/{id}")
    fun bookmarks(
        @Header("Authorization") api_token: String,
        @Path("id") id: Int
    ): Call<MessageResponse>

    @POST("bookmarks/{id}/delete")
    fun bookmarksDelete(
        @Header("Authorization") api_token: String,
        @Path("id") id: Int
    ): Call<MessageResponse>

    @GET("notifications")
    fun notifications(
        @Header("Authorization") api_token: String,
        @Query("page") page: Int
    ): Call<AllNotificationsResponse>

    @FormUrlEncoded
    @POST("search")
    fun search(
        @Header("Authorization") api_token: String,
        @Field("search") search: String
    ): Call<LectureSearchResponse>


    @FormUrlEncoded
    @POST("posts/{id}")
    fun postReaction(
        @Header("Authorization") api_token: String,
        @Path("id") id: Int,
        @Field("type") type: Int
    ): Call<MessageResponse>


    @POST("post_delete/{id}")
    fun deleteReaction(
        @Header("Authorization") api_token: String,
        @Path("id") id: Int
    ): Call<MessageResponse>

    @GET("students_base_reaction/{id}")
    fun likesStudent(
        @Header("Authorization") api_token: String,
        @Path("id") id: Int,
        @Query("page") page: Int,
        @Query("type") type: Int
    ): Call<PostReactionResponse>


    @GET("mobile_version")
    fun checkVersion(): Call<VersionResponse>
}