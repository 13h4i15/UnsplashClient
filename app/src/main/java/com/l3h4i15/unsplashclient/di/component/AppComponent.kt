package com.l3h4i15.unsplashclient.di.component

import com.l3h4i15.unsplashclient.di.module.*
import com.l3h4i15.unsplashclient.di.module.detailedcollection.DetailedCollectionModule
import com.l3h4i15.unsplashclient.di.module.detailedpicture.DetailedPictureModule
import com.l3h4i15.unsplashclient.di.module.search.SearchModule
import com.l3h4i15.unsplashclient.ui.collections.CollectionsFragment
import com.l3h4i15.unsplashclient.ui.main.MainActivity
import com.l3h4i15.unsplashclient.ui.random.RandomPictureFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelModule::class, RetrofitModule::class, RoomModule::class, RxModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)

    fun inject(fragment: RandomPictureFragment)
    fun inject(fragment: CollectionsFragment)

    fun plusDetailedCollectionComponent(module: DetailedCollectionModule): DetailedCollectionComponent
    fun plusDetailedPictureComponent(module: DetailedPictureModule): DetailedPictureComponent
    fun plusSearchComponent(module: SearchModule): SearchComponent
}