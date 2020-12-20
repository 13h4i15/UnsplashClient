package com.l3h4i15.unsplashclient.repository.delete

import com.l3h4i15.unsplashclient.database.main.Database
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteAllCollectionsRepository @Inject constructor(private val database: Database) {
    fun delete() {
        database.clearAllTables()
    }
}