package com.ricardo.swisspost.presentation.articles_list.model

import java.util.Date

data class Article(
    val key: String,
    val title: String,
    val description: String?,
    val author: String?,
    val date: Date?,
    val image: String?,
) {
    // equals overrided for unit tests. we don't want to compare the key because it's generated based on a timestamp
    override fun equals(other: Any?): Boolean {
        if (other !is Article) return false
        return this.title == other.title &&
                this.description == other.description &&
                this.author == other.author &&
                this.date == other.date &&
                this.image == other.image
    }

    override fun toString(): String {
        return "Article(title=$title, description=$description, author=$author, date=$date, image=$image)"
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (author?.hashCode() ?: 0)
        result = 31 * result + (date?.hashCode() ?: 0)
        result = 31 * result + (image?.hashCode() ?: 0)
        return result
    }
}