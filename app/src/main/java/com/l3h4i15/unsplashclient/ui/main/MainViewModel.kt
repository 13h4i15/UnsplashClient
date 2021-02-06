package com.l3h4i15.unsplashclient.ui.main

import androidx.lifecycle.ViewModel
import com.l3h4i15.unsplashclient.navigation.Navigation
import com.l3h4i15.unsplashclient.navigation.newRootFragment
import com.l3h4i15.unsplashclient.ui.random.RandomPictureFragment
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {
    fun setupRoot(navigation: Navigation) {
        navigation.newRootFragment<RandomPictureFragment>()
    }
}