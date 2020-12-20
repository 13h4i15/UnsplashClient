package com.l3h4i15.unsplashclient.repository.obtain

import com.l3h4i15.unsplashclient.converter.response.PictureResponseToModelConverter
import com.l3h4i15.unsplashclient.model.content.Picture
import com.l3h4i15.unsplashclient.network.main.Api
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ObtainRandomPictureRepository @Inject constructor(
    private val api: Api,
    private val converter: PictureResponseToModelConverter
) : ObtainRepository<Single<Picture>> {
    override fun obtain(): Single<Picture> {
        return api.getRandom()
            .map {
                converter.convert(it)
            }
    }
}