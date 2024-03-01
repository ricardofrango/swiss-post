package com.ricardo.swisspost.presentation.articles_list

import android.text.format.DateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ricardo.swisspost.R
import com.ricardo.swisspost.presentation.articles_list.model.Article
import java.util.Date

const val newsListRoute = "newsList"

fun NavGraphBuilder.articlesListScreen(navigate: (String) -> Unit) = composable(
    route = newsListRoute,
) {
    ArticlesListScreen(navigate)
}

@Composable
private fun ArticlesListScreen(
    navigate: (String) -> Unit
) {
    val context = LocalContext.current
    var dataLoaded by rememberSaveable { mutableStateOf(false) }
    val dateFormat by remember { mutableStateOf(DateFormat.getDateFormat(context)) }
    val newsListViewModel = hiltViewModel<ArticlesListViewModel>()
    val uiState by newsListViewModel.uiState.collectAsState()
    val isRefreshingState by newsListViewModel.isRefreshing.collectAsState()

    val onRefresh: () -> Unit = { newsListViewModel.refresh() }
    val onRetryClick: () -> Unit = { newsListViewModel.loadNews() }
    val onArticleClick: (Article) -> Unit = {
        // TODO
    }

    if (!dataLoaded) {
        LaunchedEffect(Unit) {
            newsListViewModel.loadNews()
            dataLoaded = true
        }
    }

    UpdateArticlesScreen(
        dateFormat = dateFormat,
        uiState = uiState,
        isRefreshingState = isRefreshingState,
        onRefresh = onRefresh,
        onRetryClick = onRetryClick,
        onArticleClick = onArticleClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UpdateArticlesScreen(
    dateFormat: java.text.DateFormat,
    uiState: ArticlesListUiState,
    isRefreshingState: Boolean,
    onRefresh: () -> Unit,
    onRetryClick: () -> Unit,
    onArticleClick: (Article) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("News Provider")
                }
            )
        },
    ) { paddingValues ->
        when (uiState) {
            ArticlesListUiState.Loading ->
                ArticlesLoading(
                    modifier = Modifier.padding(paddingValues)
                )

            is ArticlesListUiState.Success ->
                ArticlesListContent(
                    modifier = Modifier.padding(paddingValues),
                    dateFormat = dateFormat,
                    newsList = uiState.articlesList,
                    refreshing = isRefreshingState,
                    onRefresh = onRefresh,
                    onArticleClick = onArticleClick
                )

            ArticlesListUiState.Error -> ArticlesErrorLoading(
                modifier = Modifier.padding(paddingValues),
                onRetryClick = onRetryClick
            )
        }
    }
}

@Preview
@Composable
fun ArticlesErrorLoadingPreview() {
    UpdateArticlesScreen(
        dateFormat = java.text.DateFormat.getDateInstance(),
        uiState = ArticlesListUiState.Error,
        isRefreshingState = false,
        onRefresh = {},
        onRetryClick = {},
        onArticleClick = {},
    )
}

@Preview
@Composable
fun ArticlesLoadingPreview() {
    UpdateArticlesScreen(
        dateFormat = java.text.DateFormat.getDateInstance(),
        uiState = ArticlesListUiState.Loading,
        isRefreshingState = false,
        onRefresh = {},
        onRetryClick = {},
        onArticleClick = {},
    )
}

@Preview
@Composable
fun ArticlesListContentPreview() {
    UpdateArticlesScreen(
        dateFormat = java.text.DateFormat.getDateInstance(),
        uiState = ArticlesListUiState.Success(
            articlesList = listOf(
                Article(
                    key = "1",
                    title = "title title title title title title title title title title title title title title title title title title title title title title title title title title title title title title title title title title title title",
                    image = "image",
                    date = Date(),
                    description = "",
                    author = ""
                ),
                Article(
                    key = "2",
                    title = "title2",
                    image = "image2",
                    date = Date(),
                    description = "",
                    author = ""
                )
            )
        ),
        isRefreshingState = false,
        onRefresh = {},
        onRetryClick = {},
        onArticleClick = {},
    )
}

@Composable
fun ArticlesErrorLoading(
    modifier: Modifier = Modifier,
    onRetryClick: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(16.dp), text = "Error Loading Articles"
        )
        Button(onClick = onRetryClick) {
            Text(text = "Retry")
        }
    }
}

@Composable
fun ArticlesLoading(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ArticlesListContent(
    modifier: Modifier = Modifier,
    newsList: List<Article>,
    refreshing: Boolean,
    onRefresh: () -> Unit,
    dateFormat: java.text.DateFormat,
    onArticleClick: (Article) -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = onRefresh
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        LazyColumn {
            items(
                items = newsList,
                key = { it.key },
            ) {
                NewsItem(
                    item = it,
                    dateFormat = dateFormat,
                    onArticleClick = onArticleClick
                )
            }
        }
        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun NewsItem(
    item: Article,
    dateFormat: java.text.DateFormat,
    onArticleClick: (Article) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = { onArticleClick(item) }
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(2f),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.image)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.ic_error)
            )
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(9f)
            ) {
                Text(text = item.title)
                item.date?.let {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        textAlign = TextAlign.End,
                        text = "Published at ${dateFormat.format(it)}"
                    )
                }
            }
        }
    }
}
