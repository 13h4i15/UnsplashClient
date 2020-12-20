package com.l3h4i15.unsplashclient.network.model.content

import com.squareup.moshi.Json

data class PictureUrlsApiResponse(
    @field:Json(name = "full") val full: String,
    @field:Json(name = "regular") val regular: String,
    @field:Json(name = "small") val small: String
)