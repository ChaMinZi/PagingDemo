package com.example.pagingdemo.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_database")
data class KeywordEntity(
    @PrimaryKey
    @ColumnInfo(name = "record_name")
    val recordName: String,
    @ColumnInfo(name = "record_time")
    val recordTime: Long = System.currentTimeMillis()
)

fun List<KeywordEntity>.asDomainModel(): List<String> {
    return map {
        it.recordName
    }
}
