package com.l3h4i15.unsplashclient.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.l3h4i15.unsplashclient.R
import com.l3h4i15.unsplashclient.databinding.FragmentSearchResultBinding
import com.l3h4i15.unsplashclient.ui.base.BaseFragment
import com.l3h4i15.unsplashclient.ui.decoration.LinearRecyclerItemDecorator
import com.l3h4i15.unsplashclient.ui.diff.PicturesDiffUtilCallback
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

class SearchResultFragment @Inject constructor(
    private val adapter: SearchResultRecyclerAdapter,
    private val decorator: LinearRecyclerItemDecorator
) : BaseFragment() {
    override val viewModel by viewModels<SearchResultViewModel> { factory }

    override val title: Int
        get() = R.string.search_result_title

    private lateinit var binding: FragmentSearchResultBinding

    private val query: String by lazy {
        arguments?.getString(QUERY_EXTRA) ?: throw IllegalStateException()
    }
    private val id: Int? by lazy { arguments?.getInt(COLLECTION_ID_EXTRA) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) viewModel.loadNextPage(query, id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search_result, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.adapter = adapter
        binding.decorator = decorator
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        val layoutManager = LinearLayoutManager(requireContext())
        binding.manager = layoutManager

        adapter.setOnPictureClickListener { viewModel.navigatePicture(it, navigation) }
        viewModel.picturesObservable
            .subscribe {
                val callback = PicturesDiffUtilCallback(adapter.pictures, it)
                val diffResult = DiffUtil.calculateDiff(callback)
                adapter.pictures = it
                diffResult.dispatchUpdatesTo(adapter)
            }.addTo(compositeDisposable)

        binding.onScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (layoutManager.findLastVisibleItemPosition() == adapter.itemCount - 1) {
                    viewModel.loadNextPage(query, id)
                }
            }
        }

        viewModel.messageObservable
            .subscribe {
                Toast.makeText(requireContext(), R.string.loading_error_message, Toast.LENGTH_LONG)
                    .show()
            }.addTo(compositeDisposable)
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