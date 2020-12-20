package com.l3h4i15.unsplashclient.di.module.detailedpicture

import com.l3h4i15.unsplashclient.di.scope.DetailedPictureScope
import com.l3h4i15.unsplashclient.model.content.Picture
import dagger.Module
import dagger.Provides

@Module
class DetailedPictureModule(@get:Provides @DetailedPictureScope val picture: Picture)