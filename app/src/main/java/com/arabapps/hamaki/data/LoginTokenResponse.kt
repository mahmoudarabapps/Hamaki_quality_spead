package com.arabapps.hamaki.data

import com.google.gson.annotations.SerializedName

data class LoginTokenResponse(

	@field:SerializedName("token")
	val token: String? = null,
	@field:SerializedName("student")
	val student: Student? = null


)
