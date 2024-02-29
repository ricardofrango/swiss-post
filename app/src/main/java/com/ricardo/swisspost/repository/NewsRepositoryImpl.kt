package com.ricardo.swisspost.repository

import com.ricardo.swisspost.repository.model.NewsResponse
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi
) : NewsRepository {
    override suspend fun getNews(query: String?, domains: String?): NewsResponse =
        newsApi.getNews(
            query = query,
            domains = domains
        )
}
