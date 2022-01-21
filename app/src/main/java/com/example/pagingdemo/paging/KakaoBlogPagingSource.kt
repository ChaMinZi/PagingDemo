package com.example.pagingdemo.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pagingdemo.api.BlogDocuments
import com.example.pagingdemo.api.KakaoService
import com.example.pagingdemo.repository.KakaoRepository.Companion.NETWORK_PAGE_SIZE
import retrofit2.HttpException
import java.io.IOException

private const val KAKAO_STARTING_PAGE_INDEX = 1

class KakaoBlogPagingSource(
    private val service: KakaoService,
    private val query: String,
    private val sortType: String
) : PagingSource<Int, BlogDocuments>() {

    override fun getRefreshKey(state: PagingState<Int, BlogDocuments>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BlogDocuments> {
        val position = params.key ?: KAKAO_STARTING_PAGE_INDEX
        val apiQuery = query

        return try {
            val response = service.searchBlog(
                query = apiQuery,
                page = position,
                size = NETWORK_PAGE_SIZE,
                sort = sortType
            )
            val documents = response.documents
            val nextKey = if (documents.isEmpty()) {
                null
            } else {
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }

            LoadResult.Page(
                data = documents,
                prevKey = if (position == KAKAO_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}