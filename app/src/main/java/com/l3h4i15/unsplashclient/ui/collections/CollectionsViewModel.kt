package com.l3h4i15.unsplashclient.ui.collections

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.l3h4i15.unsplashclient.db.repository.CollectionsRepository
import com.l3h4i15.unsplashclient.log.LogDataContract
import com.l3h4i15.unsplashclient.network.repository.UnsplashApiRepository
import com.l3h4i15.unsplashclient.util.disposeIfPossible
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.Subject
import javax.inject.Inject

class CollectionsViewModel @Inject constructor(
    private val collectionsRepository: CollectionsRepository,
    private val unsplashApiRepository: UnsplashApiRepository
) : ViewModel() {
    val isRefreshing: ObservableBoolean = ObservableBoolean()

    private val toastSubject: Subject<Boolean> = BehaviorSubject.create()
    val toastObservable: Observable<Boolean>
        get() = toastSubject

    private var page = 1
    private var loadDisposable: Disposable? = null

    init {
        reload()
    }

    override fun onCleared() {
        dispose()
        super.onCleared()
    }

    fun loadNextPage() {
        loadPage(page)
    }

    fun onRefresh() {
        isRefreshing.set(true)
        reload()
    }

    private fun reload() {
        loadPage(1)
    }

    private fun loadPage(page: Int) {
        dispose()
        loadDisposable = unsplashApiRepository.getCollections(page)
            .doAfterTerminate {
                isRefreshing.set(false)
            }
            .subscribe({
                if (page == 1) collectionsRepository.delete()
                if (it.isNotEmpty()) {
                    this.page = page + 1
                    collectionsRepository.save(it)
                }
            }, {
                Log.e(LogDataContract.Error.LOADING_ERROR_TAG, it.toString())
                toastSubject.onNext(true)
            })
    }

    private fun dispose() {
        loadDisposable?.disposeIfPossible()
    }
}