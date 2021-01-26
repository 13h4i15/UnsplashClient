package com.l3h4i15.unsplashclient.di.module.app

import androidx.room.Room
import com.l3h4i15.unsplashclient.application.UnsplashApp
import com.l3h4i15.unsplashclient.db.main.Database
import com.l3h4i15.unsplashclient.db.main.DbDataContract
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {
    @Provides
    @Singleton
    fun database(app: UnsplashApp): Database {
        return Room.databaseBuilder(app, Database::class.java, DbDataContract.Main.DB_NAME)
            .build()
    }
}