package com.l3h4i15.unsplashclient.converter.entity

import com.l3h4i15.unsplashclient.db.entity.UserEntity
import com.l3h4i15.unsplashclient.model.User
import javax.inject.Inject

class UserEntityConverter @Inject constructor() : EntityConverter<User, UserEntity> {

    override fun convertModel(model: User): UserEntity = model.run {
        UserEntity(id, username, smallAvatar)
    }

    override fun convertSource(source: UserEntity): User = source.run {
        User(id, username, smallAvatar)
    }
}