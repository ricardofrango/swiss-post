package com.ricardo.swisspost.di

import com.ricardo.swisspost.BuildConfig
import com.ricardo.swisspost.repository.NewsApi
import com.ricardo.swisspost.repository.NewsApiKeyInterceptorImpl
import com.ricardo.swisspost.repository.NewsRepository
import com.ricardo.swisspost.repository.NewsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetworkingModule {

    @Singleton
    @Binds
    fun bindNewsRepository(newsRepository: NewsRepositoryImpl): NewsRepository

    @Singleton
    @Binds
    @NewsApiKeyInterceptor
    fun bindNewsApiKeyInterceptor(newsRepository: NewsApiKeyInterceptorImpl): Interceptor

    companion object {
        @Singleton
        @Provides
        @NewsUrl
        fun provideNewsUrl(): String = BuildConfig.NEWS_API_HOST

        @Singleton
        @Provides
        @NewsApiKey
        fun provideNewsApiKey(): String = BuildConfig.NEWS_API_KEY

        @Singleton
        @Provides
        fun provideOkHttpClient(
            @NewsApiKeyInterceptor apiKeyInterceptor: Interceptor
        ) : OkHttpClient =
            OkHttpClient.Builder()
                .addInterceptor(apiKeyInterceptor)
                .build()

        @Singleton
        @Provides
        fun provideRetrofit(
            @NewsUrl baseUrl: String,
            okHttpClient: OkHttpClient
        ): Retrofit =
            Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        @Singleton
        @Provides
        fun provideNewsApi(retrofit: Retrofit): NewsApi =
            retrofit.create(NewsApi::class.java)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NewsApiKeyInterceptor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NewsUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NewsApiKey
