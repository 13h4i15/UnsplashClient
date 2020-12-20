package com.l3h4i15.unsplashclient.network.model.content

import com.squareup.moshi.Json

data class SearchResultApiResponse(
    @field:Json(name = "total") val total: Int,
    @field:Json(name = "total_pages") val totalPages: Int,
    @field:Json(name = "results") val results: List<PictureApiResponse>
)