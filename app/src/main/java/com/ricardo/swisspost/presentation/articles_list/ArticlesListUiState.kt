package com.ricardo.swisspost.presentation.articles_list

import com.ricardo.swisspost.presentation.articles_list.model.Article

sealed class ArticlesListUiState {

    data object Loading : ArticlesListUiState()
    data class Success(val articlesList: List<Article>) : ArticlesListUiState()
    data object Error : ArticlesListUiState()
}