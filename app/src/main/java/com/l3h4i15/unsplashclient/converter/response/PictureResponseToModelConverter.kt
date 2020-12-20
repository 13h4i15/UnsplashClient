package com.l3h4i15.unsplashclient.converter.response

import com.l3h4i15.unsplashclient.converter.ObjectConverter
import com.l3h4i15.unsplashclient.model.content.Picture
import com.l3h4i15.unsplashclient.network.model.content.PictureApiResponse
import javax.inject.Inject

class PictureResponseToModelConverter @Inject constructor(
    private val converter: UserResponseToModelConverter
) : ObjectConverter<PictureApiResponse, Picture> {
    override fun convert(source: PictureApiResponse): Picture {
        return Picture(
            source.id, source.width, source.height, source.description,
            source.urls.full, source.urls.regular, source.urls.small,
            converter.convert(source.user)
        )
    }
}