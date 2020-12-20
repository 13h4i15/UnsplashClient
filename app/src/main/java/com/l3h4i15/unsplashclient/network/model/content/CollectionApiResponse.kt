package com.l3h4i15.unsplashclient.network.model.content

import com.l3h4i15.unsplashclient.network.model.user.UserResponse
import com.squareup.moshi.Json

data class CollectionApiResponse(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "user") val user: UserResponse?,
    @field:Json(name = "cover_photo") val coverPhoto: PictureApiResponse
)