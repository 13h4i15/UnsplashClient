package com.l3h4i15.unsplashclient.ui.search

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.l3h4i15.unsplashclient.log.LogDataContract
import com.l3h4i15.unsplashclient.model.Picture
import com.l3h4i15.unsplashclient.network.repository.UnsplashApiRepository
import com.l3h4i15.unsplashclient.util.disposeIfPossible
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.Subject
import javax.inject.Inject

class SearchResultViewModel @Inject constructor(
    private val repository: UnsplashApiRepository
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

    override fun onCleared() {
        dispose()
        super.onCleared()
    }

    fun loadNextPage(query: String, id: Int?) {
        dispose()
        disposable = (id?.let { repository.getSearchPictures(query, page, it) }
            ?: repository.getSearchPictures(query, page))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val pictures = picturesSubject.value?.toMutableList() ?: mutableListOf()
                if (it.isNotEmpty()) {
                    ++page
                    pictures.addAll(it)
                    picturesSubject.onNext(pictures)
                } else if (page == 1) {
                    isResultsAvailable.set(false)
                }
            }, {
                Log.e(LogDataContract.Error.LOADING_ERROR_TAG, it.toString())
                toastSubject.onNext(true)
            })
    }

    private fun dispose() {
        disposable.disposeIfPossible()
    }
}