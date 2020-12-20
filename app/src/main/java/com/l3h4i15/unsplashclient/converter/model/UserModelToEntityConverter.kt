package com.l3h4i15.unsplashclient.converter.model

import com.l3h4i15.unsplashclient.converter.ObjectConverter
import com.l3h4i15.unsplashclient.database.entity.UserEntity
import com.l3h4i15.unsplashclient.model.user.User
import javax.inject.Inject

class UserModelToEntityConverter @Inject constructor() :
    ObjectConverter<User, UserEntity> {

    override fun convert(source: User): UserEntity {
        return UserEntity(source.id, source.username, source.smallAvatar)
    }
}