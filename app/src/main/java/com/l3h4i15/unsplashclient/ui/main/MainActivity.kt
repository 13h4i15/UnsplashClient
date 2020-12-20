package com.l3h4i15.unsplashclient.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.l3h4i15.unsplashclient.R
import com.l3h4i15.unsplashclient.application.UnsplashApp
import com.l3h4i15.unsplashclient.databinding.ActivityMainBinding
import com.l3h4i15.unsplashclient.model.search.SearchRequest
import com.l3h4i15.unsplashclient.ui.collectionpictures.CollectionPicturesFragment
import com.l3h4i15.unsplashclient.ui.collections.CollectionsFragment
import com.l3h4i15.unsplashclient.ui.detailed.DetailedPictureFragment
import com.l3h4i15.unsplashclient.ui.random.RandomPictureFragment
import com.l3h4i15.unsplashclient.ui.search.SearchResultFragment
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    private val viewModel by viewModels<MainViewModel> { factory }
    private val unsplashApp: UnsplashApp by lazy { application as UnsplashApp }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        unsplashApp.appComponent.inject(this)

        setSupportActionBar(binding.toolbar)
        binding.onNavigationClickListener = View.OnClickListener {
            onBackPressed()
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, RandomPictureFragment())
                .commit()
        }

        viewModel.viewObservable
            .subscribe {
                supportActionBar?.title = when (it) {
                    is MainViewState.Random -> getString(R.string.random_picture_title)
                    is MainViewState.Collections -> getString(R.string.collections_title)
                    is MainViewState.Pictures -> it.collection.title
                    is MainViewState.Detailed -> getString(R.string.detailed_picture_title)
                    is MainViewState.Search -> getString(R.string.search_result_title)
                }

                supportActionBar?.setDisplayHomeAsUpEnabled(it !is MainViewState.Random && it !is MainViewState.Collections)

            }
            .addTo(compositeDisposable)

        viewModel.navigationObservable
            .subscribe {
                val transaction = supportFragmentManager.beginTransaction()
                val fragment = when (it) {
                    NavigationViewState.COLLECTIONS -> CollectionsFragment()
                    NavigationViewState.COLLECTION_PICTURES -> CollectionPicturesFragment()
                    NavigationViewState.DETAILED -> DetailedPictureFragment()
                    NavigationViewState.SEARCH_RESULT -> SearchResultFragment()
                    else -> throw IllegalStateException()
                }
                transaction.replace(R.id.fragment, fragment)

                if (it != NavigationViewState.COLLECTIONS) {
                    transaction.addToBackStack(null)
                }

                transaction.commit()
            }
            .addTo(compositeDisposable)
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (viewModel.viewState is MainViewState.Search && supportFragmentManager.backStackEntryCount == 1) {
            supportFragmentManager.popBackStack()
            viewModel.setNavigationState(NavigationViewState.COLLECTIONS)
        } else if (supportFragmentManager.backStackEntryCount != 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu ?: return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.toolbar, menu)

        val searchItem = menu.findItem(R.id.search_menu_item)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrBlank()) return false

                val state = viewModel.viewState
                val request = if (state is MainViewState.Pictures) {
                    SearchRequest(query, state.collection.id)
                } else {
                    SearchRequest(query)
                }

                unsplashApp.setupSearchComponent(request)
                viewModel.setNavigationState(NavigationViewState.SEARCH_RESULT)
                searchItem.collapseActionView()

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        viewModel.viewObservable
            .map { it !is MainViewState.Search && it !is MainViewState.Detailed }
            .subscribe {
                searchItem.isVisible = it
            }
            .addTo(compositeDisposable)

        return super.onCreateOptionsMenu(menu)
    }
}