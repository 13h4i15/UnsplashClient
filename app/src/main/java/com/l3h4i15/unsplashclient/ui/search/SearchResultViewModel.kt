package com.l3h4i15.unsplashclient.ui.search

import android.util.Log
import androidx.core.os.bundleOf
import androidx.databinding.ObservableBoolean
import com.l3h4i15.unsplashclient.log.LogDataContract
import com.l3h4i15.unsplashclient.model.Picture
import com.l3h4i15.unsplashclient.navigation.Navigation
import com.l3h4i15.unsplashclient.navigation.add
import com.l3h4i15.unsplashclient.network.repository.UnsplashApiRepository
import com.l3h4i15.unsplashclient.ui.base.BaseViewModel
import com.l3h4i15.unsplashclient.ui.detailed.DetailedPictureFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

class SearchResultViewModel @Inject constructor(
    private val repository: UnsplashApiRepository
) : BaseViewModel() {
    val isResultsAvailable: ObservableBoolean = ObservableBoolean(true)

    private val picturesSubject: BehaviorSubject<List<Picture>> = BehaviorSubject.create()
    val picturesObservable: Observable<List<Picture>>
        get() = picturesSubject
    private val pictures: List<Picture>
        get() = picturesSubject.value ?: listOf()

    private var page: Int = 1
    private var disposable: Disposable? = null

    fun navigatePicture(picture: Picture, navigation: Navigation) {
        navigation.add<DetailedPictureFragment>(
            bundleOf(DetailedPictureFragment.PICTURE_EXTRA to picture)
        )
    }

    fun loadNextPage(query: String, id: Int?) {
        if (disposable?.isDisposed == false) return
        disposable = (id?.let { repository.getSearchPictures(query, page, it) }
            ?: repository.getSearchPictures(query, page))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isNotEmpty()) {
                    ++page
                    pictures.toMutableList().apply { addAll(it) }
                        .let { pictures -> picturesSubject.onNext(pictures) }
                } else if (page == 1) {
                    isResultsAvailable.set(false)
                }
            }, {
                Log.e(LogDataContract.Error.LOADING_ERROR_TAG, it.toString())
                onMessage()
            })
    }
}