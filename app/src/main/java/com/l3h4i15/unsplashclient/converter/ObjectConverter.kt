package com.l3h4i15.unsplashclient.converter

interface ObjectConverter<S, T> {
    fun convert(source: S): T
}