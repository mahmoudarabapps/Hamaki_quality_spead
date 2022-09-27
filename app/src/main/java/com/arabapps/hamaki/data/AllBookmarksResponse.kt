package com.arabapps.hamaki.data

import com.google.gson.annotations.SerializedName

data class AllBookmarksResponse(

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("data")
	val data: List<LecturesItem?>? = null,

	@field:SerializedName("links")
	val links: List<LinksItem?>? = null,

	@field:SerializedName("current_page")
	val currentPage: Int? = null
)


/*

data class BootmarkDataItem(

	@field:SerializedName("doctor")
	val doctor: Doctor? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("group_id")
	val groupId: Int? = null,

	@field:SerializedName("image_path")
	val imagePath: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
*/
