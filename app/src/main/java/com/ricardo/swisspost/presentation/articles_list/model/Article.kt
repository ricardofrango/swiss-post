package com.ricardo.swisspost.presentation.articles_list.model

import java.util.Date

data class Article(
    val key: String,
    val title: String,
    val description: String?,
    val author: String?,
    val date: Date?,
    val image: String?,
)