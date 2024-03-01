package com.ricardo.swisspost.repository

import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi
) : NewsRepository {

    override suspend fun <NEWS_MAPPER : ArticlesMapper<NEWS_RESULT>, NEWS_RESULT> getHeadlines(
        mapper: NEWS_MAPPER,
        country: String,
    ): NEWS_RESULT =
        mapper.map(
            newsApi.getHeadlines(
                country = country,
            )
        )

}
