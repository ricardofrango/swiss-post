package com.ricardo.swisspost.repository.model

import androidx.annotation.Keep

@Keep
data class ArticlesResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleResponse>
)