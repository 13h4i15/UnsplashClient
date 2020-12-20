package com.l3h4i15.unsplashclient.repository.obtain

import com.l3h4i15.unsplashclient.converter.entity.CollectionRelationToModelConverter
import com.l3h4i15.unsplashclient.database.main.Dao
import com.l3h4i15.unsplashclient.model.content.Collection
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObtainCollectionsRepository @Inject constructor(
    dao: Dao,
    converter: CollectionRelationToModelConverter
) : ObtainRepository<Observable<List<Collection>>> {
    private val observable: Observable<List<Collection>> = dao.obtainCollections()
        .map { it.map { collection -> converter.convert(collection) } }
        .map { it.reversed() }

    override fun obtain(): Observable<List<Collection>> {
        return observable
    }
}