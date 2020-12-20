package com.l3h4i15.unsplashclient.listener

import com.l3h4i15.unsplashclient.model.content.Collection

@FunctionalInterface
fun interface OnCollectionClickListener {
    fun onClick(collection: Collection)
}