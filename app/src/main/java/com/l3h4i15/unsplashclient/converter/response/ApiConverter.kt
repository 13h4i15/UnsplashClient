package com.l3h4i15.unsplashclient.converter.response

interface ApiConverter<S, T> {
    fun convert(source: S): T
}