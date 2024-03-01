package com.ricardo.swisspost.presentation.articles_list

import com.ricardo.swisspost.presentation.articles_list.model.Article
import com.ricardo.swisspost.repository.ArticlesMapper
import com.ricardo.swisspost.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ArticlesListViewModelTest {

    @Mock
    lateinit var articlesMapper: ArticlesMapper<List<Article>>

    @Mock
    lateinit var newsRepository: NewsRepository

    private lateinit var underTestViewModel: ArticlesListViewModel

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        // 1
        Dispatchers.setMain(testDispatcher)

        MockitoAnnotations.openMocks(this)

        underTestViewModel = ArticlesListViewModel(
            articlesMapper = articlesMapper,
            newsRepository = newsRepository
        )
    }

    @After
    fun tearDown() {
        // 2
        Dispatchers.resetMain()
        // 3
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `test initial view model loading with success`() = runTest {
        val headlinesAnswer = listOf<Article>()

        whenever(newsRepository.getHeadlines(any<ArticlesMapper<List<Article>>>(), any()))
            .thenAnswer { headlinesAnswer }

        underTestViewModel.loadNews()

        val state = underTestViewModel.uiState.value
        val refresh = underTestViewModel.isRefreshing.value
        assertThat("state not success", state is ArticlesListUiState.Success)
        assertThat(
            "state list different",
            (state as ArticlesListUiState.Success).articlesList == headlinesAnswer
        )
        assertThat("state is refreshing", !refresh)
    }

    @Test
    fun `test initial view model loading with error`() = runTest {
        val exception = RuntimeException()

        whenever(newsRepository.getHeadlines(any<ArticlesMapper<List<Article>>>(), any()))
            .thenThrow(exception)

        underTestViewModel.loadNews()

        val state = underTestViewModel.uiState.value
        val refresh = underTestViewModel.isRefreshing.value
        assertThat("state not error", state is ArticlesListUiState.Error)
        assertThat("state is refreshing", !refresh)
    }

    @Test
    fun `test refresh view model with success`() = runTest {
        val headlinesAnswer = listOf<Article>()
        val listRefreshingState = mutableListOf<Boolean>()

        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            underTestViewModel.isRefreshing.toList(listRefreshingState)
        }

        whenever(newsRepository.getHeadlines(any<ArticlesMapper<List<Article>>>(), any()))
            .thenAnswer { headlinesAnswer }

        underTestViewModel.refresh()

        val state = underTestViewModel.uiState.value

        assertThat("isRefreshing states wrong", listRefreshingState == listOf(false, true, false))
        assertThat(
            "state not success",
            state is ArticlesListUiState.Success && state.articlesList == headlinesAnswer
        )

        collectJob.cancel()
    }

    @Test
    fun `test refresh view model with error`() = runTest {
        val exception = RuntimeException()
        val listRefreshingState = mutableListOf<Boolean>()

        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            underTestViewModel.isRefreshing.toList(listRefreshingState)
        }

        whenever(newsRepository.getHeadlines(any<ArticlesMapper<List<Article>>>(), any()))
            .thenThrow(exception)

        underTestViewModel.refresh()

        val state = underTestViewModel.uiState.value

        assertThat("isRefreshing states wrong", listRefreshingState == listOf(false, true, false))
        assertThat("state not error", state is ArticlesListUiState.Error)

        collectJob.cancel()
    }
}