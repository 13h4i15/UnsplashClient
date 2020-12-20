package com.l3h4i15.unsplashclient.listener

import com.l3h4i15.unsplashclient.model.content.Picture

@FunctionalInterface
fun interface OnPictureClickListener {
    fun onClick(picture: Picture)
}