package com.l3h4i15.unsplashclient.db.main

import androidx.room.*
import androidx.room.Dao
import com.l3h4i15.unsplashclient.db.entity.CollectionEntity
import com.l3h4i15.unsplashclient.db.entity.UserEntity
import com.l3h4i15.unsplashclient.db.relation.CollectionWithUserRelation
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

@Dao
abstract class Dao {
    @Query("SELECT * FROM COLLECTION")
    @Transaction
    abstract fun observeCollections(): Observable<List<CollectionWithUserRelation>>

    @Query("SELECT * FROM COLLECTION WHERE ID = :id")
    @Transaction
    abstract fun selectCollection(id: Int): Single<CollectionWithUserRelation>

    @Query("DELETE FROM USER")
    abstract fun deleteUsers()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(collection: CollectionEntity)

    @Transaction
    open fun insertCollections(collections: List<CollectionWithUserRelation>) {
        collections.forEach {
            it.user?.let { user -> insert(user) }
            insert(it.collection)
        }
    }
}