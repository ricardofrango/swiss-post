package com.ricardo.swisspost.repository

import com.ricardo.swisspost.repository.model.NewsResponse

interface NewsRepository {
    suspend fun getNews(
        query: String? = null,
        domains: String? = null
    ): NewsResponse
}