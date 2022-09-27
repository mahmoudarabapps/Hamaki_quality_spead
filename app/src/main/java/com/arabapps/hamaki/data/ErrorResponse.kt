package com.arabapps.hamaki.data

import com.google.gson.annotations.SerializedName

data class ErrorResponse(

	@field:SerializedName("error")
	val error: String? = null
)
