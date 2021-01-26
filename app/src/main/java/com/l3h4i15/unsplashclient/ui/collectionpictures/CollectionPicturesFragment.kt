package com.l3h4i15.unsplashclient.ui.collectionpictures

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.l3h4i15.unsplashclient.R
import com.l3h4i15.unsplashclient.db.repository.CollectionsRepository
import com.l3h4i15.unsplashclient.databinding.FragmentCollectionPicturesBinding
import com.l3h4i15.unsplashclient.navigation.Navigation
import com.l3h4i15.unsplashclient.navigation.add
import com.l3h4i15.unsplashclient.ui.detailed.DetailedPictureFragment
import com.l3h4i15.unsplashclient.ui.search.SearchResultFragment
import com.l3h4i15.unsplashclient.util.diff.PicturesDiffUtilCallback
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class CollectionPicturesFragment @Inject constructor(
    private val factory: ViewModelProvider.Factory,
    private val collectionsRepository: CollectionsRepository,
    private val navigation: Navigation,
    private val compositeDisposable: CompositeDisposable,
    private val adapter: CollectionPicturesRecyclerAdapter
) : Fragment() {
    private lateinit var binding: FragmentCollectionPicturesBinding

    private val collectionId: Int by lazy {
        arguments?.getInt(COLLECTION_ID_EXTRA) ?: throw IllegalStateException()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_collection_pictures, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectionsRepository.get(collectionId)
            .subscribeOn(Schedulers.io())
            .subscribe({
                navigation.setTitle(it.title)
            }, {})

        val viewModel by viewModels<CollectionPicturesViewModel> { factory }
        if (savedInstanceState == null) viewModel.loadNextPage(collectionId)

        binding.adapter = adapter
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        val layoutManager =
            GridLayoutManager(requireContext(), resources.getInteger(R.integer.pictures_span_count))
        binding.manager = layoutManager

        adapter.setOnPictureClickListener {
            navigation.add<DetailedPictureFragment>(
                bundleOf(DetailedPictureFragment.PICTURE to it)
            )
        }

        viewModel.picturesObservable
            .subscribe {
                val callback = PicturesDiffUtilCallback(adapter.pictures, it)
                val diffResult = DiffUtil.calculateDiff(callback)
                adapter.pictures = it
                diffResult.dispatchUpdatesTo(adapter)
            }
            .addTo(compositeDisposable)

        binding.onScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (layoutManager.findLastVisibleItemPosition() == adapter.itemCount - 1) {
                    viewModel.loadNextPage(collectionId)
                }
            }
        }

        viewModel.toastObservable
            .filter { it }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Toast.makeText(requireContext(), R.string.loading_error_message, Toast.LENGTH_LONG)
                    .show()
            }
            .addTo(compositeDisposable)
    }

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search_menu_item -> {
                val searchView = item.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (query.isNullOrBlank()) return false
                        navigation.add<SearchResultFragment>(
                            bundleOf(
                                SearchResultFragment.QUERY_EXTRA to query,
                                SearchResultFragment.COLLECTION_ID_EXTRA to collectionId
                            )
                        )
                        item.collapseActionView()
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }
                })
                true
            }
            else -> false
        }
    }

    companion object {
        const val COLLECTION_ID_EXTRA = "id"
    }
}