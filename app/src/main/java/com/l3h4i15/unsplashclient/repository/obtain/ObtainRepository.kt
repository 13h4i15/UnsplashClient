package com.l3h4i15.unsplashclient.repository.obtain

interface ObtainRepository<T> {
    fun obtain(): T
}