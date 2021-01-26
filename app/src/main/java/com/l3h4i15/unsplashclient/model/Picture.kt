package com.l3h4i15.unsplashclient.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Picture(
    val id: String,
    val width: Int,
    val height: Int,
    val description: String?,
    val fullUrl: String,
    val regularUrl: String,
    val smallUrl: String,
    val user: User
) : Parcelable