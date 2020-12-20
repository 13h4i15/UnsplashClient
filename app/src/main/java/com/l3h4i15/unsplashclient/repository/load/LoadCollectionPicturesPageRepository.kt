package com.l3h4i15.unsplashclient.repository.load

import com.l3h4i15.unsplashclient.converter.response.PictureResponseToModelConverter
import com.l3h4i15.unsplashclient.di.scope.DetailedCollectionScope
import com.l3h4i15.unsplashclient.model.content.Collection
import com.l3h4i15.unsplashclient.model.content.Picture
import com.l3h4i15.unsplashclient.network.main.Api
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@DetailedCollectionScope
class LoadCollectionPicturesPageRepository @Inject constructor(
    private val api: Api,
    private val collection: Collection,
    private val converter: PictureResponseToModelConverter
) : LoadPageRepository<List<Picture>> {

    override fun load(page: Int): Single<List<Picture>> {
        return api.getCollectionPicturePage(collection.id, page)
            .map { it.map { picture -> converter.convert(picture) } }
    }
}