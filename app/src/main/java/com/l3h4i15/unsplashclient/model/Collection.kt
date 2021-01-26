package com.l3h4i15.unsplashclient.model

data class Collection(
    val id: Int,
    val title: String,
    val description: String?,
    val user: User?,
    val smallUrl: String
)