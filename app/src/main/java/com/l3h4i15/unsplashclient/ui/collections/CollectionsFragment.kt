package com.l3h4i15.unsplashclient.ui.collections

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.l3h4i15.unsplashclient.R
import com.l3h4i15.unsplashclient.application.UnsplashApp
import com.l3h4i15.unsplashclient.databinding.FragmentCollectionsBinding
import com.l3h4i15.unsplashclient.repository.obtain.ObtainCollectionsRepository
import com.l3h4i15.unsplashclient.ui.decoration.LinearRecyclerItemDecorator
import com.l3h4i15.unsplashclient.ui.main.MainViewModel
import com.l3h4i15.unsplashclient.ui.main.MainViewState
import com.l3h4i15.unsplashclient.ui.main.NavigationViewState
import com.l3h4i15.unsplashclient.util.diff.CollectionsDiffUtilCallback
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

class CollectionsFragment : Fragment() {
    @Inject
    lateinit var obtainRepository: ObtainCollectionsRepository

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    @Inject
    lateinit var adapter: CollectionsRecyclerAdapter

    @Inject
    lateinit var decorator: LinearRecyclerItemDecorator

    private lateinit var binding: FragmentCollectionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_collections, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val unsplashApp = (requireActivity().application as UnsplashApp)
        unsplashApp.appComponent.inject(this)

        val viewModel by viewModels<CollectionsViewModel> { factory }
        binding.viewModel = viewModel

        val activityViewModel by activityViewModels<MainViewModel> { factory }
        activityViewModel.setViewState(MainViewState.Collections)
        adapter.setOnCollectionClickListener {
            unsplashApp.setupDetailedCollectionComponent(it)
            activityViewModel.setNavigationState(NavigationViewState.COLLECTION_PICTURES)
        }

        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY


        obtainRepository.obtain()
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

        binding.adapter = adapter
        binding.decorator = decorator

        binding.onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
            viewModel.onRefresh()
        }

        binding.onScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                if (lastVisibleItemPosition == adapter.itemCount - 1) {
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

    companion object {
        @JvmStatic
        @BindingAdapter("decorator")
        fun addDecorator(view: RecyclerView, decorator: LinearRecyclerItemDecorator) {
            view.addItemDecoration(decorator)
        }
    }
}