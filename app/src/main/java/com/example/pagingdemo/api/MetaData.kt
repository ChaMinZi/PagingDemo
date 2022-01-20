package com.example.pagingdemo.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MetaData(
    @Json(name = "total_count") val totalCount: Int?,
    @Json(name = "pageable_count") val pageableCount: Int?,
    @Json(name = "is_end") val isEnd: Boolean?
)