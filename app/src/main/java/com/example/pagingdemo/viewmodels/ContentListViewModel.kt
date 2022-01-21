package com.example.pagingdemo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pagingdemo.database.KeywordEntity
import com.example.pagingdemo.models.ItemModel
import com.example.pagingdemo.repository.KakaoRepository
import com.example.pagingdemo.repository.KeywordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContentListViewModel @Inject constructor(
    private val kakaoRepository: KakaoRepository,
    private val keywordRepository: KeywordRepository
) : ViewModel() {

    var selectedPostType = 0

    fun updateSelectedPostType(selected: Int) {
        selectedPostType = selected
    }

    private val _filterType = MutableLiveData(0)
    val filterType: LiveData<Int> get() = _filterType

    fun updateFilter(type: Int) {
        _filterType.postValue(type)
    }

    private fun getFilterTypeString(): String {
        return if (filterType.value == 0) "accuracy" else "recency"
    }

    /**
     * Paging
     **/
    private var currentFilterType: Int = 0

    private var currentCafeQuery: String? = null
    private var currentCafeResult: Flow<PagingData<ItemModel>>? = null

    fun searchCafe(queryString: String): Flow<PagingData<ItemModel>> {
        val lastResult = currentCafeResult
        if (queryString == currentCafeQuery && currentFilterType == filterType.value && lastResult != null) {
            return lastResult
        }

        currentFilterType = filterType.value!!
        currentCafeQuery = queryString
        val newResult: Flow<PagingData<ItemModel>> =
            kakaoRepository.getCafeResultStream(queryString, getFilterTypeString())
                .cachedIn(viewModelScope)

        currentCafeResult = newResult
        return newResult
    }

    private var currentBlogQuery: String? = null
    private var currentBlogResult: Flow<PagingData<ItemModel>>? = null

    fun searchBlog(queryString: String): Flow<PagingData<ItemModel>> {
        val lastResult = currentBlogResult
        if (queryString == currentBlogQuery && currentFilterType == filterType.value && lastResult != null) {
            return lastResult
        }

        currentBlogQuery = queryString
        val newResult: Flow<PagingData<ItemModel>> =
            kakaoRepository.getBlogResultStream(queryString, getFilterTypeString())
                .cachedIn(viewModelScope)

        currentBlogResult = newResult
        return newResult
    }

//    private var currentMergeQuery: String? = null
//    private var currentMergeResult: Flow<PagingData<ItemModel>>? = null
//
//    fun searchAll(queryString: String): Flow<PagingData<ItemModel>> {
//        val lastResult = currentMergeResult
//        if (queryString == currentMergeQuery && lastResult != null) {
//            return lastResult
//        }
//
//        currentMergeQuery = queryString
//        val newBlogResult: Flow<PagingData<ItemModel>> =
//            kakaoRepository.getBlogResultStream(queryString)
//        val newCafeResult: Flow<PagingData<ItemModel>> =
//            kakaoRepository.getCafeResultStream(queryString)
//
//        val newMergeResult =
//            flowOf(newBlogResult, newCafeResult).flattenMerge().cachedIn(viewModelScope)
//
//        currentMergeResult = newMergeResult
//        return newMergeResult
//    }

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