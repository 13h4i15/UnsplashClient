package com.l3h4i15.unsplashclient.network.repository

import com.l3h4i15.unsplashclient.converter.response.ApiConverter
import com.l3h4i15.unsplashclient.model.Collection
import com.l3h4i15.unsplashclient.model.Picture
import com.l3h4i15.unsplashclient.network.main.Api
import com.l3h4i15.unsplashclient.network.model.content.CollectionApiResponse
import com.l3h4i15.unsplashclient.network.model.content.PictureApiResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashApiRepositoryImpl @Inject constructor(
    private val api: Api,
    private val pictureConverter: ApiConverter<PictureApiResponse, Picture>,
    private val collectionConverter: ApiConverter<CollectionApiResponse, Collection>
) : UnsplashApiRepository {
    override fun getRandom(): Single<Picture> = api.getRandom().map { pictureConverter.convert(it) }

    override fun getCollectionPictures(id: Int, page: Int): Single<List<Picture>> =
        api.getCollectionPictures(id, page)
            .map { it.map { picture -> pictureConverter.convert(picture) } }

    override fun getCollections(page: Int): Single<List<Collection>> = api.getCollections(page)
        .map { it.map { collection -> collectionConverter.convert(collection) } }

    override fun getSearchPictures(query: String, page: Int): Single<List<Picture>> =
        api.getSearchPictures(query, page).map {
            it.results.map { picture -> pictureConverter.convert(picture) }
        }

    override fun getSearchPictures(query: String, page: Int, collectionId: Int)
            : Single<List<Picture>> = api.getSearchPictures(query, page, collectionId).map {
        it.results.map { picture -> pictureConverter.convert(picture) }
    }
}