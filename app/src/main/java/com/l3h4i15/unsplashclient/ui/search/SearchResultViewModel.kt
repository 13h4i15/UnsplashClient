package com.l3h4i15.unsplashclient.ui.search

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.l3h4i15.unsplashclient.log.LogDataContract
import com.l3h4i15.unsplashclient.model.content.Picture
import com.l3h4i15.unsplashclient.repository.load.LoadSearchResultPageRepository
import com.l3h4i15.unsplashclient.util.disposeIfPossible
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.Subject
import javax.inject.Inject

class SearchResultViewModel @Inject constructor(
    private val repository: LoadSearchResultPageRepository,
    private val compositeDisposable: CompositeDisposable,
) : ViewModel() {
    val isResultsAvailable: ObservableBoolean = ObservableBoolean(true)

    private val picturesSubject = BehaviorSubject.create<List<Picture>>()
    val picturesObservable: Observable<List<Picture>>
        get() = picturesSubject

    private val toastSubject: Subject<Boolean> = BehaviorSubject.create()
    val toastObservable: Observable<Boolean>
        get() = toastSubject

    private var page: Int = 1

    private var disposable: Disposable? = null

    init {
        loadNextPage()
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun loadNextPage() {
        disposable?.disposeIfPossible()
        disposable = repository.load(page)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val pictures = picturesSubject.value?.toMutableList() ?: mutableListOf()
                if (it.isNotEmpty()) {
                    ++page
                    pictures.addAll(it)
                } else if (page == 1) {
                    isResultsAvailable.set(false)
                }
                picturesSubject.onNext(pictures)
            }, {
                Log.e(LogDataContract.Error.LOADING_ERROR_TAG, it.toString())
                toastSubject.onNext(true)
            })
            .addTo(compositeDisposable)
    }
}