package com.l3h4i15.unsplashclient.network.repository

import com.l3h4i15.unsplashclient.model.Collection
import com.l3h4i15.unsplashclient.model.Picture
import io.reactivex.rxjava3.core.Single

interface UnsplashApiRepository {
    fun getRandom(): Single<Picture>

    fun getCollectionPictures(id: Int, page: Int): Single<List<Picture>>

    fun getCollections(page: Int): Single<List<Collection>>

    fun getSearchPictures(query: String, page: Int): Single<List<Picture>>

    fun getSearchPictures(query: String, page: Int, collectionId: Int): Single<List<Picture>>
}