package com.ricardo.swisspost.repository

import com.ricardo.swisspost.repository.model.ArticlesResponse

interface ArticlesMapper<NEWS_RESULT> {
    fun map(articlesResponse: ArticlesResponse) : NEWS_RESULT
}