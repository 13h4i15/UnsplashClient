package com.l3h4i15.unsplashclient.repository.save

import com.l3h4i15.unsplashclient.converter.model.CollectionModelToEntityConverter
import com.l3h4i15.unsplashclient.converter.model.UserModelToEntityConverter
import com.l3h4i15.unsplashclient.database.main.Dao
import com.l3h4i15.unsplashclient.database.main.Database
import com.l3h4i15.unsplashclient.model.content.Collection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveCollectionRepository @Inject constructor(
    private val dao: Dao,
    private val database: Database,
    private val userConverter: UserModelToEntityConverter,
    private val collectionConverter: CollectionModelToEntityConverter
) : SaveRepository<Collection> {
    override fun save(source: Collection) {
        database.runInTransaction {
            source.user?.let { dao.insert(userConverter.convert(it)) }
            dao.insert(collectionConverter.convert(source))
        }
    }
}