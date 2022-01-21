package com.example.pagingdemo.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoService {

    @GET("/v2/search/blog")
    suspend fun searchBlog(
        @Header("Authorization") apiKey: String = ("KakaoAK $API_KEY"),
        @Query("query") query: String,
        @Query("sort") sort: String = "accuracy",
        @Query("page") page: Int,
        @Query("size") size: Int
    ): BlogSearchResponse

    @GET("/v2/search/cafe")
    suspend fun searchCafe(
        @Header("Authorization") apiKey: String = ("KakaoAK $API_KEY"),
        @Query("query") query: String,
        @Query("sort") sort: String = "accuracy",
        @Query("page") page: Int,
        @Query("size") size: Int
    ): CafeSearchResponse

    companion object {
        private const val API_KEY = "cf985255b7b7b4352176956958b45b03"
    }
}