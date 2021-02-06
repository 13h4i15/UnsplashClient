package com.l3h4i15.unsplashclient.ui.collectionpictures

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.l3h4i15.unsplashclient.R
import com.l3h4i15.unsplashclient.databinding.FragmentCollectionPicturesBinding
import com.l3h4i15.unsplashclient.ui.base.BaseFragment
import com.l3h4i15.unsplashclient.ui.diff.PicturesDiffUtilCallback
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

class CollectionPicturesFragment @Inject constructor(
    private val adapter: CollectionPicturesRecyclerAdapter
) : BaseFragment() {
    override val viewModel by viewModels<CollectionPicturesViewModel> { factory }

    override val hasOptionsMenu: Boolean
        get() = true

    private lateinit var binding: FragmentCollectionPicturesBinding

    private val collectionId: Int by lazy {
        arguments?.getInt(COLLECTION_ID_EXTRA) ?: throw IllegalStateException()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) viewModel.loadNextPage(collectionId)
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
        viewModel.evaluateTitle(collectionId, navigation)

        binding.adapter = adapter
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        val layoutManager =
            GridLayoutManager(requireContext(), resources.getInteger(R.integer.pictures_span_count))
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
                    viewModel.loadNextPage(collectionId)
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
        inflater.inflate(R.menu.search_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search_menu_item -> {
                val searchView = item.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (query.isNullOrBlank()) return false
                        viewModel.navigateSearch(query, collectionId, navigation)
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