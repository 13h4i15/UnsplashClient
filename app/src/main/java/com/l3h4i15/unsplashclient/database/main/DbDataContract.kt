package com.l3h4i15.unsplashclient.database.main

interface DbDataContract {
    object Main {
        const val DB_NAME = "unsplash"
    }

    object Collection {
        const val TABLE_NAME = "collection"

        const val ID = "id"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val USER_ID = "user_id"
        const val SMALL_URL = "small_url"
    }

    object User {
        const val TABLE_NAME = "user"

        const val ID = "id"
        const val USERNAME = "username"
        const val SMALL_AVATAR = "small_avatar"
    }
}