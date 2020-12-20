package com.l3h4i15.unsplashclient.repository.load

import com.l3h4i15.unsplashclient.converter.response.SearchResultResponseToModelConverter
import com.l3h4i15.unsplashclient.di.scope.SearchScope
import com.l3h4i15.unsplashclient.model.content.Picture
import com.l3h4i15.unsplashclient.model.search.SearchRequest
import com.l3h4i15.unsplashclient.network.main.Api
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@SearchScope
class LoadSearchResultPageRepository @Inject constructor(
    private val api: Api,
    private val searchRequest: SearchRequest,
    private val converter: SearchResultResponseToModelConverter
) : LoadPageRepository<List<Picture>> {
    override fun load(page: Int): Single<List<Picture>> {
        val query = if (searchRequest.collectionId != null) {
            api.getSearchPicturesPage(searchRequest.query, page, searchRequest.collectionId)
        } else {
            api.getSearchPicturesPage(searchRequest.query, page)
        }
        return query.map { converter.convert(it) }.map { it.results }
    }
}