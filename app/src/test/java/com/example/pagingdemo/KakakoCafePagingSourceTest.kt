package com.example.pagingdemo

import androidx.paging.PagingSource
import com.example.pagingdemo.api.CafeDocuments
import com.example.pagingdemo.paging.KakaoCafePagingSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class KakakoCafePagingSourceTest {

    private val mockReturns = listOf(
        CafeDocuments()
    )

    private var mockApi = FakeKakaoApi().apply {
        mockReturns.forEach { cafe -> addPost(cafe) }
    }

    @Before
    fun setUp() {
    }

    @Test
    fun loadReturnsPageWhenOnSuccessfulLoadOfItemKeyedData() = runBlocking {
        val pagingSource = KakaoCafePagingSource(mockApi, "우유", "accuracy")
        assertEquals(
            PagingSource.LoadResult.Page(
                data = listOf(mockReturns[0]),
                prevKey = null,
                nextKey = 1
            ),
            pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )
        )
    }

}