package com.example.pagingdemo.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.pagingdemo.database.KeywordEntity
import com.example.pagingdemo.database.SearchDatabase
import com.example.pagingdemo.database.asDomainModel
import javax.inject.Inject

class KeywordRepository @Inject constructor(private val database: SearchDatabase) {
    val keywords: LiveData<List<String>> =
        Transformations.map(database.keywordDao.getAllKeyword()) {
            it.asDomainModel()
        }

    suspend fun updateKeyword(keyword: KeywordEntity) {
        database.keywordDao.insert(keyword)
    }

    suspend fun deleteKeywordOverLimit() {
        database.keywordDao.deleteLast()
    }
}