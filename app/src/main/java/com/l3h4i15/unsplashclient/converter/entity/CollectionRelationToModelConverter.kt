package com.l3h4i15.unsplashclient.converter.entity

import com.l3h4i15.unsplashclient.converter.ObjectConverter
import com.l3h4i15.unsplashclient.database.relation.CollectionWithUserRelation
import com.l3h4i15.unsplashclient.model.content.Collection
import javax.inject.Inject

class CollectionRelationToModelConverter @Inject constructor(
    private val converter: UserEntityToModelConverter
) : ObjectConverter<CollectionWithUserRelation, Collection> {

    override fun convert(source: CollectionWithUserRelation): Collection {
        val user = if (source.user != null) converter.convert(source.user) else null
        val collection = source.collection
        return Collection(
            collection.id, collection.title, collection.description, user, collection.smallUrl
        )
    }
}