package com.l3h4i15.unsplashclient.di.module.search

import androidx.lifecycle.ViewModel
import com.l3h4i15.unsplashclient.di.mapkey.ViewModelKey
import com.l3h4i15.unsplashclient.ui.search.SearchResultViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SearchViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SearchResultViewModel::class)
    internal abstract fun bindSearchResultViewModel(viewModel: SearchResultViewModel): ViewModel
}