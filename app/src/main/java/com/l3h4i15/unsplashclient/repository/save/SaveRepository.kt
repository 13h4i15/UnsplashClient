package com.l3h4i15.unsplashclient.repository.save

interface SaveRepository<S> {
    fun save(source: S)
}