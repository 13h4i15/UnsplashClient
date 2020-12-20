package com.l3h4i15.unsplashclient.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ContextModule(@get:Provides @Singleton val context: Context)