package com.l3h4i15.unsplashclient.converter.response

import com.l3h4i15.unsplashclient.model.Picture
import com.l3h4i15.unsplashclient.model.User
import com.l3h4i15.unsplashclient.network.model.content.PictureApiResponse
import com.l3h4i15.unsplashclient.network.model.user.UserResponse
import javax.inject.Inject

class PictureResponseToModelConverter @Inject constructor(
    private val converter: ApiConverter<UserResponse, User>
) : ApiConverter<PictureApiResponse, Picture> {
    override fun convert(source: PictureApiResponse): Picture = source.run {
        Picture(
            id, width, height, description, urls.full, urls.regular, urls.small,
            converter.convert(user)
        )
    }
}