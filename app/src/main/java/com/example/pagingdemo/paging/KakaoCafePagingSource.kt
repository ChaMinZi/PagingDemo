package com.example.pagingdemo.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pagingdemo.api.CafeDocuments
import com.example.pagingdemo.api.KakaoService
import com.example.pagingdemo.repository.KakaoRepository.Companion.NETWORK_PAGE_SIZE
import retrofit2.HttpException
import java.io.IOException

private const val KAKAO_STARTING_PAGE_INDEX = 1

class KakaoCafePagingSource(
    private val service: KakaoService,
    private val query: String
) : PagingSource<Int, CafeDocuments>() {

    override fun getRefreshKey(state: PagingState<Int, CafeDocuments>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CafeDocuments> {
        val position = params.key ?: KAKAO_STARTING_PAGE_INDEX
        val apiQuery = query

        return try {
            val response = service.searchCafe(
                query = apiQuery,
                page = position,
                size = NETWORK_PAGE_SIZE
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