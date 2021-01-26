package com.l3h4i15.unsplashclient.ui.random

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.l3h4i15.unsplashclient.log.LogDataContract
import com.l3h4i15.unsplashclient.model.Picture
import com.l3h4i15.unsplashclient.network.repository.UnsplashApiRepository
import com.l3h4i15.unsplashclient.util.disposeIfPossible
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import javax.inject.Inject

class RandomPictureViewModel @Inject constructor(
    private val repository: UnsplashApiRepository
) : ViewModel() {
    val picture: ObservableField<Picture> = ObservableField()

    private val messageSubject: Subject<Boolean> = PublishSubject.create()
    val messageObservable: Observable<Boolean>
        get() = messageSubject

    private var disposable: Disposable? = null

    init {
        onLoadAction()
    }

    override fun onCleared() {
        dispose()
        super.onCleared()
    }

    fun onLoadAction() {
        dispose()
        disposable = repository.getRandom()
            .observeOn(Schedulers.io())
            .subscribe(picture::set) {
                Log.e(LogDataContract.Error.LOADING_ERROR_TAG, it.toString())
                messageSubject.onNext(true)
            }
    }

    private fun dispose() = disposable.disposeIfPossible()
}