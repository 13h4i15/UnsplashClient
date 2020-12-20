package com.l3h4i15.unsplashclient.network.main

import com.l3h4i15.unsplashclient.network.model.content.CollectionApiResponse
import com.l3h4i15.unsplashclient.network.model.content.PictureApiResponse
import com.l3h4i15.unsplashclient.network.model.content.SearchResultApiResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("photos/random")
    fun getRandom(): Single<PictureApiResponse>

    @GET("collections")
    fun getCollectionsPage(@Query("page") page: Int): Single<List<CollectionApiResponse>>

    @GET("collections/{id}/photos")
    fun getCollectionPicturePage(@Path(value = "id") id: Int, @Query("page") page: Int):
            Single<List<PictureApiResponse>>

    @GET("search/photos")
    fun getSearchPicturesPage(@Query("query") query: String, @Query("page") page: Int):
            Single<SearchResultApiResponse>

    @GET("search/photos")
    fun getSearchPicturesPage(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("collections") collectionId: Int
    ): Single<SearchResultApiResponse>
}