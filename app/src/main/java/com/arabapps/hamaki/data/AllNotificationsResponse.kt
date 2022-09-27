package com.arabapps.hamaki.data

import com.google.gson.annotations.SerializedName

data class AllNotificationsResponse(

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("data")
    val notificationData: List<NotificationDataItem?>? = null,

    @field:SerializedName("current_page")
    val currentPage: Int? = null
)

data class NotificationDataItem(

    @field:SerializedName("is_read")
    val isRead: String? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("image_path")
    val imagePath: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("content")
    val content: String? = null,
    @field:SerializedName("created_at")
    val createdat: String? = null
)
