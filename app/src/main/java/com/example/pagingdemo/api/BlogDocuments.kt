package com.example.pagingdemo.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BlogDocuments(
    val title: String = "",
    val contents: String = "",
    val url: String = "",
    @Json(name = "blogname") val blogName: String = "",
    val thumbnail: String = "",
    val datetime: String = ""
)