package com.l3h4i15.unsplashclient.di.module.activity

import com.l3h4i15.unsplashclient.R
import com.l3h4i15.unsplashclient.di.scope.ActivityScope
import com.l3h4i15.unsplashclient.navigation.Navigation
import com.l3h4i15.unsplashclient.navigation.NavigationDispatcher
import com.l3h4i15.unsplashclient.ui.main.MainActivity
import dagger.Module
import dagger.Provides

@Module
class NavigationModule {
    @Provides
    @ActivityScope
    fun navigation(activity: MainActivity): Navigation =
        NavigationDispatcher(activity, R.id.fragment)
}