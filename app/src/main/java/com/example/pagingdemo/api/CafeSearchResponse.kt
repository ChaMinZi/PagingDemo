package com.example.pagingdemo.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CafeSearchResponse(
    @Json(name = "meta")
    val metaData: MetaData?,
    @Json(name = "documents")
    val documents: List<CafeDocuments> = emptyList()
)