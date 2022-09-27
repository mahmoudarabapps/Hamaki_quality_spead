package com.arabapps.hamaki.data

import com.google.gson.annotations.SerializedName

data class LectureByIDResponse(

	@field:SerializedName("subject_id")
	val subjectId: Int? = null,

	@field:SerializedName("subject")
	val subject: SubjectsItem? = null,

	@field:SerializedName("image_path")
	val imagePath: String? = null,

	@field:SerializedName("video_url")
	val video480: String? = null,

	@field:SerializedName("video_url_2")
	val video720: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("bookmark")
	val bookmark: String? = null,

	@field:SerializedName("description")
	val description: String? = null,


	@field:SerializedName("files")
	val files: List<FilesItem?>? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class FilesItem(

	@field:SerializedName("file_path")
	val filePath: String? = null,

	@field:SerializedName("lecture_id")
	val lectureId: Int? = null,

/*	@field:SerializedName("file")
	val file: String? = null,*/

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
