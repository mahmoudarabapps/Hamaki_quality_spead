package com.arabapps.hamaki.data

import com.google.gson.annotations.SerializedName

class VersionResponse(

    @field:SerializedName("android_version")
    val android_version: String?,

    @field:SerializedName("android_status")
    val version_status: String?

)
