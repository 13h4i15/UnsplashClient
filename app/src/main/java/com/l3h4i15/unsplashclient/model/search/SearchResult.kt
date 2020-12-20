package com.l3h4i15.unsplashclient.model.search

import com.l3h4i15.unsplashclient.model.content.Picture

data class SearchResult(
    val total: Int,
    val totalPages: Int,
    val results: List<Picture>
)