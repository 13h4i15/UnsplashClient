package com.l3h4i15.unsplashclient.converter.entity

interface EntityConverter<M, S> {
    fun convertModel(model: M): S
    fun convertSource(source: S): M
}