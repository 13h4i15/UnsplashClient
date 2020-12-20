package com.l3h4i15.unsplashclient.ui.collections

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.l3h4i15.unsplashclient.log.LogDataContract
import com.l3h4i15.unsplashclient.repository.delete.DeleteAllCollectionsRepository
import com.l3h4i15.unsplashclient.repository.load.LoadCollectionsPageRepository
import com.l3h4i15.unsplashclient.repository.save.SaveCollectionRepository
import com.l3h4i15.unsplashclient.util.disposeIfPossible
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.Subject
import javax.inject.Inject

class CollectionsViewModel @Inject constructor(
    private val loadRepository: LoadCollectionsPageRepository,
    private val saveRepository: SaveCollectionRepository,
    private val deleteRepository: DeleteAllCollectionsRepository,
    private val compositeDisposable: CompositeDisposable
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
        compositeDisposable.clear()
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
        loadDisposable?.disposeIfPossible()
        loadDisposable = loadRepository.load(page)
            .doAfterTerminate {
                isRefreshing.set(false)
            }
            .subscribe({
                if (page == 1) deleteRepository.delete()
                if (it.isNotEmpty()) {
                    this.page = page + 1
                    it.forEach { collection -> saveRepository.save(collection) }
                }
            }, {
                Log.e(LogDataContract.Error.LOADING_ERROR_TAG, it.toString())
                toastSubject.onNext(true)
            })
            .addTo(compositeDisposable)
    }
}