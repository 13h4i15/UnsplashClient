package com.l3h4i15.unsplashclient.converter.entity

import com.l3h4i15.unsplashclient.db.entity.CollectionEntity
import com.l3h4i15.unsplashclient.db.entity.UserEntity
import com.l3h4i15.unsplashclient.db.relation.CollectionWithUserRelation
import com.l3h4i15.unsplashclient.model.Collection
import com.l3h4i15.unsplashclient.model.User
import javax.inject.Inject

class CollectionRelationConverter @Inject constructor(
    private val userConverter: EntityConverter<User, UserEntity>,
) : EntityConverter<Collection, CollectionWithUserRelation> {
    override fun convertModel(model: Collection): CollectionWithUserRelation =
        CollectionWithUserRelation(
            getCollectionEntity(model),
            model.user?.let { userConverter.convertModel(it) })

    override fun convertSource(source: CollectionWithUserRelation): Collection {
        val user = source.user?.let { userConverter.convertSource(source.user) }
        return source.collection.run { Collection(id, title, description, user, smallUrl) }
    }

    private fun getCollectionEntity(collection: Collection): CollectionEntity =
        collection.run { CollectionEntity(id, title, description, user?.id, smallUrl) }
}