package com.l3h4i15.unsplashclient.network.model.user

import com.squareup.moshi.Json

data class UserResponse(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "username") val username: String,
    @field:Json(name = "profile_image") val userAvatar: UserAvatarResponse
)