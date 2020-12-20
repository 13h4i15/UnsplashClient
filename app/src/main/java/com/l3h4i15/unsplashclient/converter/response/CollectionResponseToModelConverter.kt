package com.l3h4i15.unsplashclient.converter.response

import com.l3h4i15.unsplashclient.converter.ObjectConverter
import com.l3h4i15.unsplashclient.model.content.Collection
import com.l3h4i15.unsplashclient.network.model.content.CollectionApiResponse
import javax.inject.Inject

class CollectionResponseToModelConverter @Inject constructor(
    private val userConverter: UserResponseToModelConverter,
    private val pictureConverter: PictureResponseToModelConverter
) : ObjectConverter<CollectionApiResponse, Collection> {

    override fun convert(source: CollectionApiResponse): Collection {
        val user = if (source.user != null) userConverter.convert(source.user) else null
        val picture = pictureConverter.convert(source.coverPhoto)
        return Collection(
            source.id,
            source.title,
            source.description,
            user,
            picture.smallUrl
        )
    }
}