package com.l3h4i15.unsplashclient.di.module

import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.disposables.CompositeDisposable

@Module
class RxModule {
    @Provides
    fun compositeDisposable(): CompositeDisposable = CompositeDisposable()
}