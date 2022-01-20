package com.example.pagingdemo.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CafeDocuments(
    val title: String = "",
    val contents: String = "",
    val url: String = "",
    @Json(name = "cafename") val cafeName: String = "",
    val thumbnail: String = "",
    val datetime: String = ""
)