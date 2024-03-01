package com.ricardo.swisspost.repository.model

import androidx.annotation.Keep

@Keep
data class ArticleResponse(
    val source: SourceResponse?,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String,
)
