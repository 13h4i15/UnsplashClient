package com.l3h4i15.unsplashclient.listener

import com.l3h4i15.unsplashclient.model.Collection
import com.l3h4i15.unsplashclient.model.Picture

typealias OnCollectionClick = (collection: Collection) -> Unit

typealias OnPictureClick = (picture: Picture) -> Unit