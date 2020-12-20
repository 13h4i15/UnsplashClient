package com.l3h4i15.unsplashclient.model.content

import com.l3h4i15.unsplashclient.model.user.User

data class Picture(
    val id: String,
    val width: Int,
    val height: Int,
    val description: String?,
    val fullUrl: String,
    val regularUrl: String,
    val smallUrl: String,
    val user: User
)