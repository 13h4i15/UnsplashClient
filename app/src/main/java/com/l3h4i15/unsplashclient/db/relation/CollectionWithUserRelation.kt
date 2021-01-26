package com.l3h4i15.unsplashclient.db.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.l3h4i15.unsplashclient.db.entity.CollectionEntity
import com.l3h4i15.unsplashclient.db.entity.UserEntity
import com.l3h4i15.unsplashclient.db.main.DbDataContract

data class CollectionWithUserRelation(
    @Embedded val collection: CollectionEntity,
    @Relation(
        parentColumn = DbDataContract.Collection.USER_ID,
        entityColumn = DbDataContract.User.ID,
        entity = UserEntity::class
    )
    val user: UserEntity?
)