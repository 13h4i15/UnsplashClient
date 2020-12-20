package com.l3h4i15.unsplashclient.ui.search

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
import com.l3h4i15.unsplashclient.R
import com.l3h4i15.unsplashclient.application.UnsplashApp
import com.l3h4i15.unsplashclient.databinding.FragmentSearchResultBinding
import com.l3h4i15.unsplashclient.ui.decoration.LinearRecyclerItemDecorator
import com.l3h4i15.unsplashclient.ui.main.MainViewModel
import com.l3h4i15.unsplashclient.ui.main.MainViewState
import com.l3h4i15.unsplashclient.ui.main.NavigationViewState
import com.l3h4i15.unsplashclient.util.diff.PicturesDiffUtilCallback
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

class SearchResultFragment : Fragment() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var adapter: SearchResultRecyclerAdapter

    @Inject
    lateinit var decorator: LinearRecyclerItemDecorator

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    private lateinit var binding: FragmentSearchResultBinding

    private val unsplashApp: UnsplashApp by lazy { requireActivity().application as UnsplashApp }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search_result, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val component = unsplashApp.searchComponent ?: return
        component.inject(this)

        val activityViewModel by activityViewModels<MainViewModel> { factory }
        activityViewModel.setViewState(MainViewState.Search)
        adapter.setOnPictureClickListener {
            unsplashApp.setupDetailedPictureComponent(it)
            activityViewModel.setNavigationState(NavigationViewState.DETAILED)
        }
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        val viewModel by viewModels<SearchResultViewModel> { factory }
        binding.viewModel = viewModel

        viewModel.picturesObservable
            .subscribe {
                val callback = PicturesDiffUtilCallback(adapter.pictures, it)
                val diffResult = DiffUtil.calculateDiff(callback)
                adapter.pictures = it
                diffResult.dispatchUpdatesTo(adapter)
            }
            .addTo(compositeDisposable)

        binding.adapter = adapter
        binding.decorator = decorator
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

    override fun onDestroy() {
        if (!requireActivity().isChangingConfigurations) unsplashApp.freeSearchComponent()
        super.onDestroy()
    }

    companion object {
        @JvmStatic
        @BindingAdapter("decorator")
        fun addDecorator(view: RecyclerView, decorator: LinearRecyclerItemDecorator) {
            view.addItemDecoration(decorator)
        }
    }
}