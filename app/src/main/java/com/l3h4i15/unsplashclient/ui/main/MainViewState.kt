package com.l3h4i15.unsplashclient.ui.main

import com.l3h4i15.unsplashclient.model.content.Collection

sealed class MainViewState {
    object Random : MainViewState()
    object Collections : MainViewState()
    class Pictures(val collection: Collection) : MainViewState()
    object Detailed : MainViewState()
    object Search : MainViewState()
}