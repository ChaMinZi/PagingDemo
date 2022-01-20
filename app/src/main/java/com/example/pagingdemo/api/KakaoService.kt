package com.example.pagingdemo.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

private const val BASE_URL = "https://dapi.kakao.com"

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

    @GET("/v2/search/cafe")
    suspend fun testCafe(
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

object Network {
    private val logger = HttpLoggingInterceptor {
        //Log.d("API", it)
    }.apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val client = OkHttpClient.Builder().addInterceptor(logger).build()
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val retrofit: KakaoService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(KakaoService::class.java)
    }

    val testApi: KakaoService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(KakaoService::class.java)
    }
}