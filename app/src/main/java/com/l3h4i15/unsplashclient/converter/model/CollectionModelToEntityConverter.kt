package com.l3h4i15.unsplashclient.converter.model

import com.l3h4i15.unsplashclient.converter.ObjectConverter
import com.l3h4i15.unsplashclient.database.entity.CollectionEntity
import com.l3h4i15.unsplashclient.model.content.Collection
import javax.inject.Inject

class CollectionModelToEntityConverter @Inject constructor() :
    ObjectConverter<Collection, CollectionEntity> {

    override fun convert(source: Collection): CollectionEntity {
        return CollectionEntity(
            source.id, source.title, source.description, source.user?.id, source.smallUrl
        )
    }
}