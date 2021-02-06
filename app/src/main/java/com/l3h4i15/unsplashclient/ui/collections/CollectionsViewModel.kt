package com.l3h4i15.unsplashclient.ui.collections

import android.util.Log
import androidx.core.os.bundleOf
import androidx.databinding.ObservableBoolean
import com.l3h4i15.unsplashclient.db.repository.CollectionsRepository
import com.l3h4i15.unsplashclient.log.LogDataContract
import com.l3h4i15.unsplashclient.model.Collection
import com.l3h4i15.unsplashclient.navigation.Navigation
import com.l3h4i15.unsplashclient.navigation.add
import com.l3h4i15.unsplashclient.network.repository.UnsplashApiRepository
import com.l3h4i15.unsplashclient.ui.base.BaseViewModel
import com.l3h4i15.unsplashclient.ui.collectionpictures.CollectionPicturesFragment
import com.l3h4i15.unsplashclient.ui.search.SearchResultFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

class CollectionsViewModel @Inject constructor(
    private val collectionsRepository: CollectionsRepository,
    private val unsplashApiRepository: UnsplashApiRepository
) : BaseViewModel() {
    val isRefreshing: ObservableBoolean = ObservableBoolean()

    private val collectionsSubject: BehaviorSubject<List<Collection>> = BehaviorSubject.create()
    private val collections: List<Collection>
        get() = collectionsSubject.value ?: listOf()
    val collectionsObservable: Observable<List<Collection>>
        get() = collectionsSubject

    private var page = 1
    private var disposable: Disposable? = null

    fun onCreate() {
        reload()
        collectionsRepository.observe()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                collections.toMutableList()
                    .apply { addAll(it.minus(this)) }
                    .let { collectionsSubject.onNext(it) }
            }.addTo(compositeDisposable)
    }

    fun navigateCollection(collection: Collection, navigation: Navigation) {
        navigation.add<CollectionPicturesFragment>(
            bundleOf(CollectionPicturesFragment.COLLECTION_ID_EXTRA to collection.id)
        )
    }

    fun navigateSearch(query: String, navigation: Navigation) {
        navigation.add<SearchResultFragment>(bundleOf(SearchResultFragment.QUERY_EXTRA to query))
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
        if (disposable?.isDisposed == false) return
        disposable = unsplashApiRepository.getCollections(page)
            .doAfterTerminate { isRefreshing.set(false) }
            .subscribe({
                if (page == 1) collectionsRepository.delete()
                if (it.isNotEmpty()) {
                    this.page = page + 1
                    collectionsRepository.save(it)
                }
            }, {
                Log.e(LogDataContract.Error.LOADING_ERROR_TAG, it.toString())
                onMessage()
            }).addTo(compositeDisposable)
    }

}