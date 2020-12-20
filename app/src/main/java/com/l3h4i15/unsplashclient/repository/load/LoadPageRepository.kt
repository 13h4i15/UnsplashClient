package com.l3h4i15.unsplashclient.repository.load

import io.reactivex.rxjava3.core.Single

interface LoadPageRepository<T> {
    fun load(page: Int): Single<T>
}