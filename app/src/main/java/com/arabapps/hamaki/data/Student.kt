package com.arabapps.hamaki.data

import com.google.gson.annotations.SerializedName

data class Student(

	@field:SerializedName("full_name")
	val fullName: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("group_id")
	val groupId: Int? = null,

	@field:SerializedName("image_path")
	val imagePath: String? = null,

	@field:SerializedName("hash_number")
	val hashNumber: String? = null,

	@field:SerializedName("last_name")
	val lastName: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("first_name")
	val firstName: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("group")
	val group: Group? = null
)
