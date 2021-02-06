package com.l3h4i15.unsplashclient.ui.base

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.l3h4i15.unsplashclient.navigation.Navigation
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {
    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    private val messageSubject: Subject<Boolean> = PublishSubject.create()
    val messageObservable: Observable<Boolean>
        get() = messageSubject.observeOn(AndroidSchedulers.mainThread()).filter { it }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun setTitle(@StringRes title: Int, navigation: Navigation) {
        navigation.setTitle(title)
    }

    protected fun onMessage() {
        messageSubject.onNext(true)
    }
}