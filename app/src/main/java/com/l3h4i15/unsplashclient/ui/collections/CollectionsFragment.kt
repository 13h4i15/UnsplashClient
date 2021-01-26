package com.l3h4i15.unsplashclient.ui.collections

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.l3h4i15.unsplashclient.R
import com.l3h4i15.unsplashclient.db.repository.CollectionsRepository
import com.l3h4i15.unsplashclient.databinding.FragmentCollectionsBinding
import com.l3h4i15.unsplashclient.navigation.Navigation
import com.l3h4i15.unsplashclient.navigation.add
import com.l3h4i15.unsplashclient.ui.collectionpictures.CollectionPicturesFragment
import com.l3h4i15.unsplashclient.ui.decoration.LinearRecyclerItemDecorator
import com.l3h4i15.unsplashclient.ui.search.SearchResultFragment
import com.l3h4i15.unsplashclient.util.diff.CollectionsDiffUtilCallback
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

class CollectionsFragment @Inject constructor(
    private val factory: ViewModelProvider.Factory,
    private val compositeDisposable: CompositeDisposable,
    private val navigation: Navigation,
    private val repository: CollectionsRepository,
    private val adapter: CollectionsRecyclerAdapter,
    private val decorator: LinearRecyclerItemDecorator
) : Fragment() {
    private lateinit var binding: FragmentCollectionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_collections, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigation.setTitle(R.string.collections_title)

        val viewModel by viewModels<CollectionsViewModel> { factory }
        binding.viewModel = viewModel

        binding.adapter = adapter
        binding.decorator = decorator
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        repository.observe()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val newCollections = adapter.collections.toMutableList()
                newCollections.addAll(it.minus(adapter.collections))
                val callback = CollectionsDiffUtilCallback(adapter.collections, newCollections)
                val diffResult = DiffUtil.calculateDiff(callback)
                adapter.collections = newCollections
                diffResult.dispatchUpdatesTo(adapter)
            }
            .addTo(compositeDisposable)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.manager = layoutManager

        adapter.setOnCollectionClickListener {
            navigation.add<CollectionPicturesFragment>(
                bundleOf(CollectionPicturesFragment.COLLECTION_ID_EXTRA to it.id)
            )
        }

        binding.onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
            viewModel.onRefresh()
        }

        binding.onScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (layoutManager.findLastVisibleItemPosition() == adapter.itemCount - 1) {
                    viewModel.loadNextPage()
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
                        navigation.add<SearchResultFragment>(bundleOf(SearchResultFragment.QUERY_EXTRA to query))
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
        @JvmStatic
        @BindingAdapter("decorator")
        fun addDecorator(view: RecyclerView, decorator: LinearRecyclerItemDecorator) {
            view.addItemDecoration(decorator)
        }
    }
}