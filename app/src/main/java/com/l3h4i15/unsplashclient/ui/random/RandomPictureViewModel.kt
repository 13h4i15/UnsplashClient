package com.l3h4i15.unsplashclient.ui.random

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.l3h4i15.unsplashclient.log.LogDataContract
import com.l3h4i15.unsplashclient.model.content.Picture
import com.l3h4i15.unsplashclient.repository.obtain.ObtainRandomPictureRepository
import com.l3h4i15.unsplashclient.util.disposeIfPossible
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import javax.inject.Inject

class RandomPictureViewModel @Inject constructor(
    private val repository: ObtainRandomPictureRepository,
    private val compositeDisposable: CompositeDisposable
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
        compositeDisposable.clear()
        super.onCleared()
    }

    fun onLoadAction() {
        disposable?.disposeIfPossible()
        disposable = repository.obtain()
            .observeOn(Schedulers.io())
            .subscribe({
                picture.set(it)
            }, {
                Log.e(LogDataContract.Error.LOADING_ERROR_TAG, it.toString())
                messageSubject.onNext(true)
            })
            .addTo(compositeDisposable)
    }
}