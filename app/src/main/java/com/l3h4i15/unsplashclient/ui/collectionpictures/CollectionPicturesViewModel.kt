package com.l3h4i15.unsplashclient.ui.collectionpictures

import android.util.Log
import androidx.core.os.bundleOf
import com.l3h4i15.unsplashclient.R
import com.l3h4i15.unsplashclient.db.repository.CollectionsRepository
import com.l3h4i15.unsplashclient.log.LogDataContract
import com.l3h4i15.unsplashclient.model.Picture
import com.l3h4i15.unsplashclient.navigation.Navigation
import com.l3h4i15.unsplashclient.navigation.add
import com.l3h4i15.unsplashclient.network.repository.UnsplashApiRepository
import com.l3h4i15.unsplashclient.ui.base.BaseViewModel
import com.l3h4i15.unsplashclient.ui.detailed.DetailedPictureFragment
import com.l3h4i15.unsplashclient.ui.search.SearchResultFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

class CollectionPicturesViewModel @Inject constructor(
    private val apiRepository: UnsplashApiRepository,
    private val collectionsRepository: CollectionsRepository,
) : BaseViewModel() {
    private val picturesSubject: BehaviorSubject<List<Picture>> = BehaviorSubject.create()
    val picturesObservable: Observable<List<Picture>>
        get() = picturesSubject
    private val pictures: List<Picture>
        get() = picturesSubject.value ?: listOf()

    private var page: Int = 1
    private var disposable: Disposable? = null

    fun evaluateTitle(collectionId: Int, navigation: Navigation) {
        collectionsRepository.get(collectionId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.title }
            .doOnError { setTitle(R.string.pictures_title, navigation) }
            .subscribe({ navigation.setTitle(it) }, {})
    }

    fun navigatePicture(picture: Picture, navigation: Navigation) {
        navigation.add<DetailedPictureFragment>(
            bundleOf(DetailedPictureFragment.PICTURE_EXTRA to picture)
        )
    }

    fun navigateSearch(query: String, collectionId: Int, navigation: Navigation) {
        navigation.add<SearchResultFragment>(
            bundleOf(
                SearchResultFragment.QUERY_EXTRA to query,
                SearchResultFragment.COLLECTION_ID_EXTRA to collectionId
            )
        )
    }

    fun loadNextPage(id: Int) {
        if (disposable?.isDisposed == false) return
        disposable = apiRepository.getCollectionPictures(id, page)
            .observeOn(AndroidSchedulers.mainThread())
            .filter { it.isNotEmpty() }
            .subscribe({
                ++page
                pictures.toMutableList().apply { addAll(it) }
                    .let { pictures -> picturesSubject.onNext(pictures) }
            }, {
                Log.e(LogDataContract.Error.LOADING_ERROR_TAG, it.toString())
                onMessage()
            }).addTo(compositeDisposable)
    }
}