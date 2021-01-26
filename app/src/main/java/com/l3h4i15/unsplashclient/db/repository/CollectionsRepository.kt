package com.l3h4i15.unsplashclient.db.repository

import com.l3h4i15.unsplashclient.model.Collection
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface CollectionsRepository {
    fun observe(): Observable<List<Collection>>
    fun get(id: Int): Single<Collection>
    fun save(collections: List<Collection>)
    fun delete()
}