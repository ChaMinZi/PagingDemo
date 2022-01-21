package com.example.pagingdemo.di

import com.example.pagingdemo.api.KakaoService
import com.example.pagingdemo.database.SearchDatabase
import com.example.pagingdemo.repository.KakaoRepository
import com.example.pagingdemo.repository.KeywordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
class RepositoryModule {

    @Provides
    @ActivityRetainedScoped
    fun provideKakaoRepository(
        service: KakaoService
    ): KakaoRepository {
        return KakaoRepository(service)
    }

    @Provides
    @ActivityRetainedScoped
    fun provideKeywordRepository(
        database: SearchDatabase
    ): KeywordRepository {
        return KeywordRepository(database)
    }
}