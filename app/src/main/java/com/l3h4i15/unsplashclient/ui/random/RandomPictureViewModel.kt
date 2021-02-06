package com.l3h4i15.unsplashclient.ui.random

import android.util.Log
import androidx.databinding.ObservableField
import com.l3h4i15.unsplashclient.log.LogDataContract
import com.l3h4i15.unsplashclient.model.Picture
import com.l3h4i15.unsplashclient.navigation.Navigation
import com.l3h4i15.unsplashclient.navigation.newRootFragment
import com.l3h4i15.unsplashclient.network.repository.UnsplashApiRepository
import com.l3h4i15.unsplashclient.ui.base.BaseViewModel
import com.l3h4i15.unsplashclient.ui.collections.CollectionsFragment
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class RandomPictureViewModel @Inject constructor(
    private val repository: UnsplashApiRepository
) : BaseViewModel() {
    val picture: ObservableField<Picture> = ObservableField()

    fun onLoadAction() {
        repository.getRandom()
            .observeOn(Schedulers.io())
            .subscribe(picture::set) {
                Log.e(LogDataContract.Error.LOADING_ERROR_TAG, it.toString())
                onMessage()
            }.addTo(compositeDisposable)
    }

    fun navigate(navigation: Navigation) {
        navigation.newRootFragment<CollectionsFragment>()
    }
}