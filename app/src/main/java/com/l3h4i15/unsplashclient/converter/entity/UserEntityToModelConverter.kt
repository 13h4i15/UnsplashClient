package com.l3h4i15.unsplashclient.converter.entity

import com.l3h4i15.unsplashclient.converter.ObjectConverter
import com.l3h4i15.unsplashclient.database.entity.UserEntity
import com.l3h4i15.unsplashclient.model.user.User
import javax.inject.Inject

class UserEntityToModelConverter @Inject constructor() : ObjectConverter<UserEntity, User> {
    override fun convert(source: UserEntity): User {
        return User(source.id, source.username, source.smallAvatar)
    }
}