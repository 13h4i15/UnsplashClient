package com.l3h4i15.unsplashclient.di.module.search

import com.l3h4i15.unsplashclient.di.scope.SearchScope
import com.l3h4i15.unsplashclient.model.search.SearchRequest
import dagger.Module
import dagger.Provides

@Module
class SearchModule(@get:Provides @SearchScope val request: SearchRequest)