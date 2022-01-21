package com.example.pagingdemo

import com.example.pagingdemo.api.CafeDocuments
import com.example.pagingdemo.api.CafeSearchResponse
import com.example.pagingdemo.api.KakaoService
import com.example.pagingdemo.database.SearchDatabase
import com.example.pagingdemo.models.Content
import com.example.pagingdemo.repository.KakaoRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ContentListViewModelTest {

    @Mock
    private lateinit var repository: KakaoRepository

    @Mock
    private lateinit var service: KakaoService

    @Mock
    private lateinit var database: SearchDatabase


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        service = mockk()
        repository = KakaoRepository(service)
    }

    @Test
    fun confirmConstructor() {
        // 생성자 확인
        val cafeDocuments = mockk<CafeDocuments> {
            every { title } returns "title"
            every { contents } returns "contents"
            every { url } returns "http::url"
            every { cafeName } returns "cafeName"
            every { thumbnail } returns "http:thumbnail"
            every { datetime } returns "2017-05-06T00:36:45+09:00"
        }

        val content = Content(cafeDocuments)

        Assert.assertTrue(content.title == "title")
        Assert.assertTrue(content.contents == "contents")
        Assert.assertTrue(content.url == "http::url")
        Assert.assertTrue(content.name == "cafeName")
        Assert.assertTrue(content.label == "cafe")
        Assert.assertTrue(content.thumbnail == "http:thumbnail")
        Assert.assertTrue(content.dateTime == "2017-05-06T00:36:45+09:00")
    }

    @Test
    fun isCalledThree() = runTest {
        // 몇 번 호출되었는지
        val repo = mock(KakaoRepository::class.java)

        repo.getCafeResultStream("우유")

        verify(repo, atLeastOnce()).getCafeResultStream("우유")

        repo.getCafeResultStream("우유")
        repo.getCafeResultStream("우유")

        verify(repo, atLeast(3)).getCafeResultStream("우유")
    }
}