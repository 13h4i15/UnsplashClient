package com.l3h4i15.unsplashclient.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.l3h4i15.unsplashclient.database.main.DbDataContract

@Entity(
    tableName = DbDataContract.Collection.TABLE_NAME,
    primaryKeys = [DbDataContract.Collection.ID],
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = [DbDataContract.User.ID],
        childColumns = [DbDataContract.Collection.USER_ID]
    )]
)
data class CollectionEntity(
    @ColumnInfo(name = DbDataContract.Collection.ID) val id: Int,
    @ColumnInfo(name = DbDataContract.Collection.TITLE) val title: String,
    @ColumnInfo(name = DbDataContract.Collection.DESCRIPTION) val description: String?,
    @ColumnInfo(name = DbDataContract.Collection.USER_ID) val userId: String?,
    @ColumnInfo(name = DbDataContract.Collection.SMALL_URL) val smallUrl: String
)