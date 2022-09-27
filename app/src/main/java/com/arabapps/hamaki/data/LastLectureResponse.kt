package com.arabapps.hamaki.data

import com.google.gson.annotations.SerializedName

data class LastLectureResponse(

    @field:SerializedName("id")
    val id: Int? = null,
    @field:SerializedName("image_path")
    val image_path: String? = null,

    @field:SerializedName("bookmark")
    val bookmark: String? = null,
    @field:SerializedName("name")
    val name: String? = null,
    @field:SerializedName("subject")
    val subject: SubjectsItem? = null
)
