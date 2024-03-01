package com.ricardo.swisspost.presentation.article_details.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class ArticleDetails(
    val key: String,
    val title: String,
    val description: String?,
    val author: String?,
    val date: Date?,
    val image: String?,
) : Parcelable