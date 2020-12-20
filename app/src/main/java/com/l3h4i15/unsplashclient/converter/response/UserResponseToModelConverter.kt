package com.l3h4i15.unsplashclient.converter.response

import com.l3h4i15.unsplashclient.converter.ObjectConverter
import com.l3h4i15.unsplashclient.model.user.User
import com.l3h4i15.unsplashclient.network.model.user.UserResponse
import javax.inject.Inject

class UserResponseToModelConverter @Inject constructor() : ObjectConverter<UserResponse, User> {
    override fun convert(source: UserResponse): User {
        return User(source.id, source.username, source.userAvatar.small)
    }
}