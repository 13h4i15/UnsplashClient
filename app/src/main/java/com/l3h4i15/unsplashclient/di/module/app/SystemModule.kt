package com.l3h4i15.unsplashclient.di.module.app

import android.app.Application
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import dagger.Module
import dagger.Provides

@Module
class SystemModule {
    @Provides
    fun inputMethodManager(app: Application): InputMethodManager = app.getSystemService()!!
}