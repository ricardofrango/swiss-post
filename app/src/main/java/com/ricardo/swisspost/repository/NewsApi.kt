package com.ricardo.swisspost.repository

import com.ricardo.swisspost.repository.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("/v2/everything")
    suspend fun getNews(
        @Query("q") query: String? = null,
        @Query("domains") domains: String? = null
    ): NewsResponse
}