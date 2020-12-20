package com.l3h4i15.unsplashclient.di.module.detailedcollection

import com.l3h4i15.unsplashclient.di.scope.DetailedCollectionScope
import com.l3h4i15.unsplashclient.model.content.Collection
import dagger.Module
import dagger.Provides

@Module
class DetailedCollectionModule(@get:Provides @DetailedCollectionScope val collection: Collection)