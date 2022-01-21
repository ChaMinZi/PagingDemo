package com.example.pagingdemo

import com.example.pagingdemo.api.*
import com.example.pagingdemo.models.Content

class FakeKakaoApi : KakaoService {

    private val model = mutableListOf<CafeDocuments>()
    fun addPost(post: CafeDocuments) {
        model.add(post)
    }

    override suspend fun searchBlog(
        apiKey: String,
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): BlogSearchResponse {
        TODO("Not yet implemented")
    }

    override suspend fun searchCafe(
        apiKey: String,
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): CafeSearchResponse {
        return CafeSearchResponse(
            MetaData(218662, 983, false),
            listOf(CafeDocuments())
        )
    }

}