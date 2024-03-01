package com.ricardo.swisspost.presentation.articles_list.di

import com.ricardo.swisspost.presentation.articles_list.mapper.ArticlesListMapperImpl
import com.ricardo.swisspost.presentation.articles_list.model.Article
import com.ricardo.swisspost.repository.ArticlesMapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ArticlesListModule {

    @Singleton
    @Binds
    fun bindArticlesMapper(newsMapper: ArticlesListMapperImpl): ArticlesMapper<@JvmSuppressWildcards List<Article>>

}