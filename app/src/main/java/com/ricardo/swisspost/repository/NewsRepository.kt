package com.ricardo.swisspost.repository

interface NewsRepository {
    suspend fun <NEWS_MAPPER: ArticlesMapper<NEWS_RESULT>, NEWS_RESULT> getHeadlines(
        mapper: NEWS_MAPPER,
        country: String,
    ): NEWS_RESULT
}