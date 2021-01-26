package com.l3h4i15.unsplashclient.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String,
    val username: String,
    val smallAvatar: String
) : Parcelable