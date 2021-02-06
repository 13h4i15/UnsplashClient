package com.l3h4i15.unsplashclient.di.component

import com.l3h4i15.unsplashclient.application.UnsplashApp
import com.l3h4i15.unsplashclient.di.module.app.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class, SystemModule::class, AppModule::class,
        RetrofitModule::class, RoomModule::class, RxModule::class]
)
interface AppComponent : AndroidInjector<UnsplashApp> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: UnsplashApp): AppComponent
    }
}