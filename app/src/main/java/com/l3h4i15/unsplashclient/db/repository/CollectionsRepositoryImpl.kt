package com.l3h4i15.unsplashclient.db.repository

import com.l3h4i15.unsplashclient.converter.entity.EntityConverter
import com.l3h4i15.unsplashclient.db.main.Dao
import com.l3h4i15.unsplashclient.db.main.Database
import com.l3h4i15.unsplashclient.db.relation.CollectionWithUserRelation
import com.l3h4i15.unsplashclient.model.Collection
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CollectionsRepositoryImpl @Inject constructor(
    private val database: Database,
    private val converter: EntityConverter<Collection, CollectionWithUserRelation>
) : CollectionsRepository {
    private val dao: Dao = database.dao()

    private val observable: Observable<List<Collection>> by lazy {
        dao.observeCollections()
            .map { it.map { collection -> converter.convertSource(collection) } }
            .map { it.reversed() }
    }

    override fun observe(): Observable<List<Collection>> = observable

    override fun get(id: Int): Single<Collection> =
        dao.selectCollection(id).map { converter.convertSource(it) }

    override fun save(collections: List<Collection>) {
        database.runInTransaction {
            dao.insertCollections(collections.map { converter.convertModel(it) })
        }
    }

    override fun delete() {
        database.runInTransaction { database.dao().deleteUsers() }
    }
}