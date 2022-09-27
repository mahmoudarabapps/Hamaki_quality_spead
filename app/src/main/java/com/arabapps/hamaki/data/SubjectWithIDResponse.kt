package com.arabapps.hamaki.data

data class SubjectWithIDResponse(
	val image_path: String? = null,
	val updatedAt: String? = null,
	val groupId: Int? = null,
	val name: String? = null,
	val lectures: List<LecturesItem?>? = null,
	val createdAt: String? = null,
	val description: String? = null,
	val id: Int? = null
)

data class LecturesItem(
    val subjectId: Int? = null,
    val image_path: String? = null,
    val videoUrl: String? = null,
    val updatedAt: String? = null,
    val name: String? = null,
    val createdAt: String? = null,
    val description: String? = null,
    val id: Int? = null,
    var bookmark: String? = null
)

