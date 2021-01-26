package com.l3h4i15.unsplashclient.db.main

import androidx.room.Database
import androidx.room.RoomDatabase
import com.l3h4i15.unsplashclient.db.entity.CollectionEntity
import com.l3h4i15.unsplashclient.db.entity.UserEntity

@Database(entities = [CollectionEntity::class, UserEntity::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun dao(): Dao
}