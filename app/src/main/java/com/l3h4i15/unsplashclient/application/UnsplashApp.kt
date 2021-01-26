package com.l3h4i15.unsplashclient.application

import com.l3h4i15.unsplashclient.di.component.*
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class UnsplashApp : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.factory().create(this)
}