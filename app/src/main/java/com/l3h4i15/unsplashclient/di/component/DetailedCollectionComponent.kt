package com.l3h4i15.unsplashclient.di.component

import com.l3h4i15.unsplashclient.di.module.detailedcollection.DetailedCollectionModule
import com.l3h4i15.unsplashclient.di.module.detailedcollection.DetailedCollectionViewModelModule
import com.l3h4i15.unsplashclient.di.scope.DetailedCollectionScope
import com.l3h4i15.unsplashclient.ui.collectionpictures.CollectionPicturesFragment
import dagger.Subcomponent

@DetailedCollectionScope
@Subcomponent(modules = [DetailedCollectionModule::class, DetailedCollectionViewModelModule::class])
interface DetailedCollectionComponent {
    fun inject(fragment: CollectionPicturesFragment)
}