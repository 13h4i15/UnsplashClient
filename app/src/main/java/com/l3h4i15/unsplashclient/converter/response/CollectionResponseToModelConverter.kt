package com.l3h4i15.unsplashclient.converter.response

import com.l3h4i15.unsplashclient.model.Collection
import com.l3h4i15.unsplashclient.model.Picture
import com.l3h4i15.unsplashclient.model.User
import com.l3h4i15.unsplashclient.network.model.content.CollectionApiResponse
import com.l3h4i15.unsplashclient.network.model.content.PictureApiResponse
import com.l3h4i15.unsplashclient.network.model.user.UserResponse
import javax.inject.Inject

class CollectionResponseToModelConverter @Inject constructor(
    private val userConverter: ApiConverter<UserResponse, User>,
    private val pictureConverter: ApiConverter<PictureApiResponse, Picture>
) : ApiConverter<CollectionApiResponse, Collection> {
    override fun convert(source: CollectionApiResponse): Collection = source.run {
        Collection(
            id,
            title,
            description,
            user?.let { userConverter.convert(it) },
            pictureConverter.convert(coverPhoto).smallUrl
        )
    }
}