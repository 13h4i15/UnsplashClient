package com.l3h4i15.unsplashclient.di.module

import android.content.Context
import androidx.room.Room
import com.l3h4i15.unsplashclient.database.main.Dao
import com.l3h4i15.unsplashclient.database.main.Database
import com.l3h4i15.unsplashclient.database.main.DbDataContract
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ContextModule::class])
class RoomModule {
    @Provides
    @Singleton
    fun dao(database: Database): Dao {
        return database.dao()
    }

    @Provides
    @Singleton
    fun database(context: Context): Database {
        return Room.databaseBuilder(context, Database::class.java, DbDataContract.Main.DB_NAME)
            .build()
    }
}