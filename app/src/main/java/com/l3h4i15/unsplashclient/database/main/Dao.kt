package com.l3h4i15.unsplashclient.database.main

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.l3h4i15.unsplashclient.database.entity.CollectionEntity
import com.l3h4i15.unsplashclient.database.entity.UserEntity
import com.l3h4i15.unsplashclient.database.relation.CollectionWithUserRelation
import io.reactivex.rxjava3.core.Observable

@Dao
interface Dao {
    @Query("SELECT * FROM COLLECTION")
    fun obtainCollections(): Observable<List<CollectionWithUserRelation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(collections: CollectionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(picture: UserEntity)
}