package com.ricardo.swisspost.presentation.articles_list.mapper

import com.ricardo.swisspost.presentation.articles_list.model.Article
import com.ricardo.swisspost.repository.model.ArticleResponse
import com.ricardo.swisspost.repository.model.ArticlesResponse
import com.ricardo.swisspost.repository.model.SourceResponse
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat

class ArticlesListMapperImplTest {

    private lateinit var underTestMapper: ArticlesListMapperImpl

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

    @Before
    fun setup() {
        underTestMapper = ArticlesListMapperImpl()
    }

    @After
    fun tearDown() {

    }

    @Test
    fun `test articles list mapping`() {
        val response = ArticlesResponse(
            status = "status",
            totalResults = 2,
            articles = listOf(
                ArticleResponse(
                    source = SourceResponse(
                        id = "1",
                        name = "SourceName1"
                    ),
                    author = "author1",
                    title = "title1",
                    description = "description1",
                    url = "url1",
                    urlToImage = "urlToImage1",
                    publishedAt = "2020-01-01T00:00:00Z",
                    content = "content1",
                ),
                ArticleResponse(
                    source = SourceResponse(
                        id = null,
                        name = "SourceName2"
                    ),
                    author = "author2",
                    title = "title2",
                    description = "description2",
                    url = "url2",
                    urlToImage = "urlToImage2",
                    publishedAt = "2020-01-01T00:00:00Z",
                    content = "content2",
                ),
                ArticleResponse(
                    source = null,
                    author = "author3",
                    title = "title3",
                    description = "description3",
                    url = "url3",
                    urlToImage = null,
                    publishedAt = "2020-01-01",
                    content = "content3",
                ),
            )
        )

        val expectedResult = listOf(
            Article(
                key = "",
                title = "title1",
                description = "description1",
                author = "author1",
                date = dateFormat.parse("2020-01-01T00:00:00Z"),
                image = "urlToImage1",
            ),
            Article(
                key = "",
                title = "title2",
                description = "description2",
                author = "author2",
                date = dateFormat.parse("2020-01-01T00:00:00Z"),
                image = "urlToImage2",
            ),
            Article(
                key = "",
                title = "title3",
                description = "description3",
                author = "author3",
                date = null,
                image = null,
            ),
        )

        val result = underTestMapper.map(response)

        println(result)
        println(expectedResult)

        MatcherAssert.assertThat("", result == expectedResult)
    }
}