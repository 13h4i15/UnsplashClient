package com.l3h4i15.unsplashclient.network.model.content

import com.l3h4i15.unsplashclient.network.model.user.UserResponse
import com.squareup.moshi.Json

data class PictureApiResponse(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "width") val width: Int,
    @field:Json(name = "height") val height: Int,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "urls") val urls: PictureUrlsApiResponse,
    @field:Json(name = "user") val user: UserResponse
)