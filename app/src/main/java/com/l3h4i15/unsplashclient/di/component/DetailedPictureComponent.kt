package com.l3h4i15.unsplashclient.di.component

import com.l3h4i15.unsplashclient.di.module.detailedpicture.DetailedPictureModule
import com.l3h4i15.unsplashclient.di.scope.DetailedPictureScope
import com.l3h4i15.unsplashclient.ui.detailed.DetailedPictureFragment
import dagger.Subcomponent

@DetailedPictureScope
@Subcomponent(modules = [DetailedPictureModule::class])
interface DetailedPictureComponent {
    fun inject(fragment: DetailedPictureFragment)
}