package com.l3h4i15.unsplashclient.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.l3h4i15.unsplashclient.database.main.DbDataContract

@Entity(tableName = DbDataContract.User.TABLE_NAME, primaryKeys = [DbDataContract.User.ID])
data class UserEntity(
    @ColumnInfo(name = DbDataContract.User.ID) val id: String,
    @ColumnInfo(name = DbDataContract.User.USERNAME) val username: String,
    @ColumnInfo(name = DbDataContract.User.SMALL_AVATAR) val smallAvatar: String
)