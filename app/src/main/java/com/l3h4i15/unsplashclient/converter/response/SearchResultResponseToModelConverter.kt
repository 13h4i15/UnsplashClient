package com.l3h4i15.unsplashclient.converter.response

import com.l3h4i15.unsplashclient.converter.ObjectConverter
import com.l3h4i15.unsplashclient.model.search.SearchResult
import com.l3h4i15.unsplashclient.network.model.content.SearchResultApiResponse
import javax.inject.Inject

class SearchResultResponseToModelConverter @Inject constructor(private val converter: PictureResponseToModelConverter) :
    ObjectConverter<SearchResultApiResponse, SearchResult> {
    override fun convert(source: SearchResultApiResponse): SearchResult {
        return SearchResult(
            source.total, source.totalPages, source.results.map { converter.convert(it) })
    }
}