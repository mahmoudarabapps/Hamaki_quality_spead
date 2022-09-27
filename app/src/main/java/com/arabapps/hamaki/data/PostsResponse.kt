package com.arabapps.hamaki.data

data class PostsResponse(
    val total: Int? = null,
    val data: List<DataItem?>? = null,
    val from: Int? = null,
//	val links: List<LinksItem?>? = null,
    var current_page: Int
)


data class DataItem(
    val doctor: Doctor? = null,
    val image_path: String? = null,
    val groupId: Int? = null,
    var likes_count: Int? = null,
    var loves_count: Int? = null,
    var dislikes_count: Int? = null,
    var all_reactions_count: Int? = null,
    var student_reaction: Int? = null,
    val description: String? = null,
    val created_at: String? = null,
    val id: Int? = null
)

data class LinksItem(
    val active: Boolean? = null,
    val label: String? = null,
    val url: String? = null
)

