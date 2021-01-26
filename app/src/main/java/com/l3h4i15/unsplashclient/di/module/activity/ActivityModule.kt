package com.l3h4i15.unsplashclient.di.module.activity

import androidx.fragment.app.Fragment
import com.l3h4i15.unsplashclient.di.mapkey.FragmentKey
import com.l3h4i15.unsplashclient.ui.collectionpictures.CollectionPicturesFragment
import com.l3h4i15.unsplashclient.ui.collections.CollectionsFragment
import com.l3h4i15.unsplashclient.ui.detailed.DetailedPictureFragment
import com.l3h4i15.unsplashclient.ui.random.RandomPictureFragment
import com.l3h4i15.unsplashclient.ui.search.SearchResultFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ActivityModule {
    @Binds
    @IntoMap
    @FragmentKey(RandomPictureFragment::class)
    abstract fun bindRandomPictureFragment(fragment: RandomPictureFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(CollectionsFragment::class)
    abstract fun bindCollectionsFragment(fragment: CollectionsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(CollectionPicturesFragment::class)
    abstract fun bindCollectionPicturesFragment(fragment: CollectionPicturesFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(DetailedPictureFragment::class)
    abstract fun bindDetailedPictureFragment(fragment: DetailedPictureFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(SearchResultFragment::class)
    abstract fun bindSearchResultFragment(fragment: SearchResultFragment): Fragment
}