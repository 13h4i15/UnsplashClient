package com.l3h4i15.unsplashclient.di.module.detailedcollection

import androidx.lifecycle.ViewModel
import com.l3h4i15.unsplashclient.di.mapkey.ViewModelKey
import com.l3h4i15.unsplashclient.ui.collectionpictures.CollectionPicturesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DetailedCollectionViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(CollectionPicturesViewModel::class)
    internal abstract fun bindCollectionPicturesViewModel(viewModel: CollectionPicturesViewModel): ViewModel
}