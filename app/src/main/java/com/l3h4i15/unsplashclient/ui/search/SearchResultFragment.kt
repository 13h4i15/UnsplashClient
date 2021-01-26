package com.l3h4i15.unsplashclient.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.l3h4i15.unsplashclient.R
import com.l3h4i15.unsplashclient.databinding.FragmentSearchResultBinding
import com.l3h4i15.unsplashclient.navigation.Navigation
import com.l3h4i15.unsplashclient.navigation.add
import com.l3h4i15.unsplashclient.ui.decoration.LinearRecyclerItemDecorator
import com.l3h4i15.unsplashclient.ui.detailed.DetailedPictureFragment
import com.l3h4i15.unsplashclient.util.diff.PicturesDiffUtilCallback
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

class SearchResultFragment @Inject constructor(
    private val factory: ViewModelProvider.Factory,
    private val navigation: Navigation,
    private val compositeDisposable: CompositeDisposable,
    private val adapter: SearchResultRecyclerAdapter,
    private val decorator: LinearRecyclerItemDecorator
) : Fragment() {

    private lateinit var binding: FragmentSearchResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search_result, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigation.setTitle(R.string.search_result_title)

        val query = arguments?.getString(QUERY_EXTRA) ?: throw IllegalStateException()
        val id = arguments?.getInt(COLLECTION_ID_EXTRA)
        val viewModel by viewModels<SearchResultViewModel> { factory }
        if (savedInstanceState == null) viewModel.loadNextPage(query, id)

        binding.viewModel = viewModel
        binding.adapter = adapter
        binding.decorator = decorator
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        viewModel.picturesObservable
            .subscribe {
                val callback = PicturesDiffUtilCallback(adapter.pictures, it)
                val diffResult = DiffUtil.calculateDiff(callback)
                adapter.pictures = it
                diffResult.dispatchUpdatesTo(adapter)
            }
            .addTo(compositeDisposable)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.manager = layoutManager

        adapter.setOnPictureClickListener {
            navigation.add<DetailedPictureFragment>(
                bundleOf(DetailedPictureFragment.PICTURE to it)
            )
        }

        binding.onScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (layoutManager.findLastVisibleItemPosition() == adapter.itemCount - 1) {
                    viewModel.loadNextPage(query, id)
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
        const val QUERY_EXTRA = "query"
        const val COLLECTION_ID_EXTRA = "id"

        @JvmStatic
        @BindingAdapter("decorator")
        fun addDecorator(view: RecyclerView, decorator: LinearRecyclerItemDecorator) {
            view.addItemDecoration(decorator)
        }
    }
}