package com.l3h4i15.unsplashclient.application

import android.app.Application
import com.l3h4i15.unsplashclient.di.component.*
import com.l3h4i15.unsplashclient.di.module.ContextModule
import com.l3h4i15.unsplashclient.di.module.detailedcollection.DetailedCollectionModule
import com.l3h4i15.unsplashclient.di.module.detailedpicture.DetailedPictureModule
import com.l3h4i15.unsplashclient.di.module.search.SearchModule
import com.l3h4i15.unsplashclient.model.content.Collection
import com.l3h4i15.unsplashclient.model.content.Picture
import com.l3h4i15.unsplashclient.model.search.SearchRequest

class UnsplashApp : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder().contextModule(ContextModule(this)).build()
    }

    private var _detailedCollectionComponent: DetailedCollectionComponent? = null
    val detailedCollectionComponent: DetailedCollectionComponent?
        get() = _detailedCollectionComponent

    private var _detailedPictureComponent: DetailedPictureComponent? = null
    val detailedPictureComponent: DetailedPictureComponent?
        get() = _detailedPictureComponent

    private var _searchComponent: SearchComponent? = null
    val searchComponent: SearchComponent?
        get() = _searchComponent

    fun setupDetailedCollectionComponent(collection: Collection) {
        _detailedCollectionComponent = appComponent.plusDetailedCollectionComponent(
            DetailedCollectionModule(collection)
        )
    }

    fun freeDetailedCollectionComponent() {
        _detailedCollectionComponent = null
    }

    fun setupDetailedPictureComponent(picture: Picture) {
        _detailedPictureComponent = appComponent.plusDetailedPictureComponent(
            DetailedPictureModule(picture)
        )
    }

    fun freeDetailedPictureComponent() {
        _detailedPictureComponent = null
    }

    fun setupSearchComponent(searchRequest: SearchRequest) {
        _searchComponent = appComponent.plusSearchComponent(SearchModule(searchRequest))
    }

    fun freeSearchComponent() {
        _searchComponent = null
    }
}