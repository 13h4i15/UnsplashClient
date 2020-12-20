package com.l3h4i15.unsplashclient.ui.main

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {
    private val navigationSubject: Subject<NavigationViewState> = PublishSubject.create()
    val navigationObservable: Observable<NavigationViewState>
        get() = navigationSubject

    private val viewSubject = BehaviorSubject.create<MainViewState>()
    val viewObservable: Observable<MainViewState>
        get() = viewSubject
    val viewState: MainViewState?
        get() = viewSubject.value

    fun setNavigationState(state: NavigationViewState) {
        navigationSubject.onNext(state)
    }

    fun setViewState(state: MainViewState) {
        viewSubject.onNext(state)
    }
}