package com.ricardo.swisspost.presentation.article_details

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.gson.Gson
import com.ricardo.swisspost.R
import com.ricardo.swisspost.presentation.article_details.model.ArticleDetails
import java.util.Date

const val articleArgName = "article"
const val articleRoute = "articleDetails/{$articleArgName}"

fun buildArticleLink(article: ArticleDetails) =
    articleRoute.replace("{$articleArgName}", Uri.encode(Gson().toJson(article)))

class ArticleParamType : NavType<ArticleDetails>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): ArticleDetails? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, ArticleDetails::class.java)
        } else {
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): ArticleDetails {
        return Gson().fromJson(value, ArticleDetails::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: ArticleDetails) {
        bundle.putParcelable(key, value)
    }
}

fun NavGraphBuilder.articlesDetailsScreen() = composable(
    route = articleRoute,
    arguments = listOf(
        navArgument(articleArgName) {
            type = ArticleParamType()
        }
    )
) {
    val article = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        it.arguments?.getParcelable(articleArgName, ArticleDetails::class.java)
    } else {
        it.arguments?.getParcelable(articleArgName)
    }

    article?.let {
        ArticleDetailsScreen(article = it)
    } ?: ArticleDetailsScreenError()
}

@Composable
fun ArticleDetailsScreenError(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(16.dp), text = "Error Loading the Article"
        )
    }
}

@Preview
@Composable
private fun ArticleDetailsScreenPreview() {
    ArticleDetailsScreen(
        article = ArticleDetails(
            key = "key",
            title = "title",
            date = Date(),
            image = "",
            description = "description",
            author = "author",
        )
    )
}

@Preview
@Composable
private fun ArticleDetailsScreenErrorPreview() {
    ArticleDetailsScreenError()
}

@Composable
private fun ArticleDetailsScreen(
    modifier: Modifier = Modifier,
    article: ArticleDetails
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f),
            model = ImageRequest.Builder(LocalContext.current)
                .data(article.image)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.ic_error)
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = article.title
        )
        article.description?.let { description ->
            Text(
                modifier = Modifier.padding(8.dp),
                text = description
            )
        }

        article.author?.let { author ->
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = author,
                textAlign = TextAlign.End
            )
        }
    }
}