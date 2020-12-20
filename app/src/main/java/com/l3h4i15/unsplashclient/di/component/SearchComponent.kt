package com.l3h4i15.unsplashclient.di.component

import com.l3h4i15.unsplashclient.di.module.search.SearchModule
import com.l3h4i15.unsplashclient.di.module.search.SearchViewModelModule
import com.l3h4i15.unsplashclient.di.scope.SearchScope
import com.l3h4i15.unsplashclient.ui.search.SearchResultFragment
import dagger.Subcomponent

@SearchScope
@Subcomponent(modules = [SearchModule::class, SearchViewModelModule::class])
interface SearchComponent {
    fun inject(fragment: SearchResultFragment)
}