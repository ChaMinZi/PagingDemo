package com.example.pagingdemo.repository

import androidx.paging.*
import com.example.pagingdemo.api.CafeSearchResponse
import com.example.pagingdemo.api.KakaoService
import com.example.pagingdemo.models.Content
import com.example.pagingdemo.models.ItemModel
import com.example.pagingdemo.paging.KakaoBlogPagingSource
import com.example.pagingdemo.paging.KakaoCafePagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class KakaoRepository(
    private val service: KakaoService
) {

    fun getCafeResultStream(query: String): Flow<PagingData<ItemModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { KakaoCafePagingSource(service, query) }
        ).flow.map { pagingData ->
            pagingData.map {
                ItemModel.ContentItem(Content(it))
            }
        }.map {
            it.insertSeparators { before, _ ->
                when (before) {
                    null -> ItemModel.HeaderItem("HEADER")
                    else -> null
                }
            }
        }
    }

    fun getBlogResultStream(query: String): Flow<PagingData<ItemModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { KakaoBlogPagingSource(service, query) }
        ).flow.map { pagingData ->
            pagingData.map {
                ItemModel.ContentItem(Content(it))
            }
        }.map {
            it.insertSeparators { before, _ ->
                when (before) {
                    null -> ItemModel.HeaderItem("HEADER")
                    else -> null
                }
            }
        }
    }

    /**
     * test
     **/
    fun testCafe(query: String): Flow<CafeSearchResponse> {
        return flow {
            service.testCafe(
                query = query,
                page = 1,
                size = 25
            )
        }
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 25
    }
}