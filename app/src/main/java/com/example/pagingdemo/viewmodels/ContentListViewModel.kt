package com.example.pagingdemo.viewmodels

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pagingdemo.database.KeywordEntity
import com.example.pagingdemo.models.Content
import com.example.pagingdemo.models.ItemModel
import com.example.pagingdemo.repository.KakaoRepository
import com.example.pagingdemo.repository.KeywordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ContentListViewModel(
    private val kakaoRepository: KakaoRepository,
    private val keywordRepository: KeywordRepository
) : ViewModel() {

    /**
     * Paging
     **/
    private var currentCafeQuery: String? = null
    private var currentCafeResult: Flow<PagingData<ItemModel>>? = null

    fun searchCafe(queryString: String): Flow<PagingData<ItemModel>> {
        val lastResult = currentCafeResult
        if (queryString == currentCafeQuery && lastResult != null) {
            return lastResult
        }

        currentCafeQuery = queryString
        val newResult: Flow<PagingData<ItemModel>> =
            kakaoRepository.getCafeResultStream(queryString).cachedIn(viewModelScope)

        currentCafeResult = newResult
        return newResult
    }

    private var currentBlogQuery: String? = null
    private var currentBlogResult: Flow<PagingData<Content>>? = null

    fun searchBlog(queryString: String): Flow<PagingData<Content>> {
        val lastResult = currentBlogResult
        if (queryString == currentBlogQuery && lastResult != null) {
            return lastResult
        }

        currentBlogQuery = queryString
        val newResult: Flow<PagingData<Content>> =
            kakaoRepository.getBlogResultStream(queryString).cachedIn(viewModelScope)

        currentBlogResult = newResult
        return newResult
    }

    /**
     * 검색어
     **/
    val keywordList = keywordRepository.keywords

    // tool bar Submit query
    val submitQuery = MutableLiveData<String>()
    private val _isSubmit = MutableLiveData<Boolean>()
    val isSubmit: LiveData<Boolean> get() = _isSubmit

    fun updateSpinnerSelected() {
        if (!submitQuery.value.isNullOrBlank()) {
            viewModelScope.launch {
                keywordRepository.updateKeyword(
                    KeywordEntity(
                        recordName = submitQuery.value.toString(),
                        recordTime = System.currentTimeMillis()
                    )
                )
                keywordRepository.deleteKeywordOverLimit()
                _isSubmit.postValue(true)
            }
        }
    }
}