package com.l3h4i15.unsplashclient.ui.collections

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.l3h4i15.unsplashclient.R
import com.l3h4i15.unsplashclient.databinding.FragmentCollectionsBinding
import com.l3h4i15.unsplashclient.ui.base.BaseFragment
import com.l3h4i15.unsplashclient.ui.decoration.LinearRecyclerItemDecorator
import com.l3h4i15.unsplashclient.ui.diff.CollectionsDiffUtilCallback
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

class CollectionsFragment @Inject constructor(
    private val adapter: CollectionsRecyclerAdapter,
    private val decorator: LinearRecyclerItemDecorator
) : BaseFragment() {
    override val viewModel by viewModels<CollectionsViewModel> { factory }

    override val title: Int
        get() = R.string.collections_title

    override val hasOptionsMenu: Boolean
        get() = true

    private lateinit var binding: FragmentCollectionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_collections, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) viewModel.onCreate()

        binding.viewModel = viewModel

        binding.adapter = adapter
        binding.decorator = decorator
        val layoutManager = LinearLayoutManager(requireContext())
        binding.manager = layoutManager
        adapter.setOnCollectionClickListener { viewModel.navigateCollection(it, navigation) }

        viewModel.collectionsObservable.subscribe {
            val callback = CollectionsDiffUtilCallback(adapter.collections, it)
            val diffResult = DiffUtil.calculateDiff(callback)
            adapter.collections = it
            diffResult.dispatchUpdatesTo(adapter)
        }.addTo(compositeDisposable)

        binding.onRefreshListener = SwipeRefreshLayout.OnRefreshListener(viewModel::onRefresh)
        binding.onScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (layoutManager.findLastVisibleItemPosition() == adapter.itemCount - 1) {
                    viewModel.loadNextPage()
                }
            }
        }

        viewModel.messageObservable
            .subscribe {
                Toast.makeText(requireContext(), R.string.loading_error_message, Toast.LENGTH_LONG)
                    .show()
            }.addTo(compositeDisposable)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search_menu_item -> {
                (item.actionView as SearchView).setOnQueryTextListener(object :
                    SearchView.OnQueryTextListener {

                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (query.isNullOrBlank()) return false
                        viewModel.navigateSearch(query, navigation)
                        item.collapseActionView()
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }
                })
                true
            }
            else -> super.onOptionsItemSelected(item)
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