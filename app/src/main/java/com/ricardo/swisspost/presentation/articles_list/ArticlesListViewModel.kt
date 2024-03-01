package com.ricardo.swisspost.presentation.articles_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ricardo.swisspost.presentation.articles_list.model.Article
import com.ricardo.swisspost.repository.ArticlesMapper
import com.ricardo.swisspost.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ArticlesListViewModel @Inject constructor(
    private val articlesMapper: ArticlesMapper<List<Article>>,
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val isRefreshingState = MutableStateFlow(false)
    private val viewModelState = MutableStateFlow<ArticlesListUiState>(ArticlesListUiState.Loading)

    val uiState = viewModelState
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value
        )

    val isRefreshing = isRefreshingState
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            isRefreshingState.value
        )

    private fun loadNews(refresh: Boolean) {
        viewModelScope.launch {
            if (refresh) {
                isRefreshingState.update { true }
            } else {
                viewModelState.update { ArticlesListUiState.Loading }
            }

            try {
                val news = newsRepository.getHeadlines(articlesMapper, Locale.getDefault().country)
                    .sortedByDescending { it.date }

                if (refresh) isRefreshingState.update { false }

                viewModelState.update { ArticlesListUiState.Success(news) }
            } catch (exception: Throwable) {
                exception.printStackTrace()
                if (refresh) isRefreshingState.update { false }
                viewModelState.update { ArticlesListUiState.Error }
            }
        }
    }

    fun loadNews() {
        loadNews(false)
    }

    fun refresh() {
        loadNews(true)
    }
}