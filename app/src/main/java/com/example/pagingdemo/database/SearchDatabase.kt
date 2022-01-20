package com.example.pagingdemo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [KeywordEntity::class], version = 1, exportSchema = false)
abstract class SearchDatabase : RoomDatabase() {
    abstract val keywordDao: KeywordDao
}

private lateinit var INSTANCE: SearchDatabase
fun getDatabase(context: Context): SearchDatabase {
    synchronized(SearchDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                SearchDatabase::class.java,
                "search_database"
            ).build()
        }
    }
    return INSTANCE
}