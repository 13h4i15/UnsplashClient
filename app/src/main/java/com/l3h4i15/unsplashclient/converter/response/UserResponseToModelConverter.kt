package com.l3h4i15.unsplashclient.converter.response

import com.l3h4i15.unsplashclient.model.User
import com.l3h4i15.unsplashclient.network.model.user.UserResponse
import javax.inject.Inject

class UserResponseToModelConverter @Inject constructor() :
    ApiConverter<UserResponse, User> {
    override fun convert(source: UserResponse): User = source.run {
        User(id, username, userAvatar.small)
    }
}