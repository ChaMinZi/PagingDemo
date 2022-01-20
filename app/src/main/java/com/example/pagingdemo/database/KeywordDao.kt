package com.example.pagingdemo.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface KeywordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(keyword: KeywordEntity)

    @Query("DELETE FROM search_database where record_time < (SELECT min(record_time) from (SELECT * FROM search_database ORDER BY record_time DESC limit 5))")
    suspend fun deleteLast()

    @Query("SELECT * FROM search_database")
    fun getAllKeyword(): LiveData<List<KeywordEntity>>

    @Query("SELECT * FROM search_database")
    fun testAllKeyword(): List<KeywordEntity>
}