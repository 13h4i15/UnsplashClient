package com.l3h4i15.unsplashclient.network.model.user

import com.squareup.moshi.Json

data class UserAvatarResponse(
    @field:Json(name = "small") val small: String
)