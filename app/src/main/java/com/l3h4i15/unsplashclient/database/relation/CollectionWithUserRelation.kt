package com.l3h4i15.unsplashclient.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.l3h4i15.unsplashclient.database.entity.CollectionEntity
import com.l3h4i15.unsplashclient.database.entity.UserEntity
import com.l3h4i15.unsplashclient.database.main.DbDataContract

data class CollectionWithUserRelation(
    @Embedded val collection: CollectionEntity,
    @Relation(
        parentColumn = DbDataContract.Collection.USER_ID,
        entityColumn = DbDataContract.User.ID,
        entity = UserEntity::class
    )
    val user: UserEntity?
)