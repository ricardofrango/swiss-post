package com.ricardo.swisspost.presentation.articles_list.mapper

import com.ricardo.swisspost.presentation.articles_list.model.Article
import com.ricardo.swisspost.repository.ArticlesMapper
import com.ricardo.swisspost.repository.model.ArticlesResponse
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class ArticlesListMapperImpl @Inject constructor() : ArticlesMapper<@JvmSuppressWildcards List<Article>> {

    private val simpleDateFormat by lazy { SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()) }

    override fun map(articlesResponse: ArticlesResponse): List<Article> {
        val timestamp = System.currentTimeMillis()
        return articlesResponse.articles.mapIndexed { index, articleResponse ->
            Article(
                key = "$timestamp$index",
                title = articleResponse.title,
                image = articleResponse.urlToImage,
                date = runCatching { simpleDateFormat.parse(articleResponse.publishedAt) }.getOrNull(),
                description = articleResponse.description,
                author = articleResponse.author
            )
        }
    }
}