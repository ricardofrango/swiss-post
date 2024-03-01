package com.ricardo.swisspost.repository

import com.ricardo.swisspost.repository.model.ArticlesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("/v2/top-headlines")
    suspend fun getHeadlines(
        @Query("country") country: String? = null,
    ): ArticlesResponse
}