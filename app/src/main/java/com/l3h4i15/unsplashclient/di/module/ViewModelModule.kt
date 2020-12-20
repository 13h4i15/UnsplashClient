package com.l3h4i15.unsplashclient.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.l3h4i15.unsplashclient.di.mapkey.ViewModelKey
import com.l3h4i15.unsplashclient.ui.collections.CollectionsViewModel
import com.l3h4i15.unsplashclient.ui.main.MainViewModel
import com.l3h4i15.unsplashclient.ui.random.RandomPictureViewModel
import com.l3h4i15.unsplashclient.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RandomPictureViewModel::class)
    internal abstract fun bindRandomPictureViewModel(viewModel: RandomPictureViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CollectionsViewModel::class)
    internal abstract fun bindCollectionsViewModel(viewModel: CollectionsViewModel): ViewModel
}