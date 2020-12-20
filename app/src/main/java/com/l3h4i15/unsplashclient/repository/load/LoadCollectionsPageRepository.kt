package com.l3h4i15.unsplashclient.repository.load

import com.l3h4i15.unsplashclient.converter.response.CollectionResponseToModelConverter
import com.l3h4i15.unsplashclient.model.content.Collection
import com.l3h4i15.unsplashclient.network.main.Api
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadCollectionsPageRepository @Inject constructor(
    private val api: Api,
    private val converter: CollectionResponseToModelConverter
) : LoadPageRepository<List<Collection>> {
    override fun load(page: Int): Single<List<Collection>> {
        return api.getCollectionsPage(page)
            .map { it.map { collection -> converter.convert(collection) } }
    }
}