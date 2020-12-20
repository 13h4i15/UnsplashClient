package com.l3h4i15.unsplashclient.ui.random

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.l3h4i15.unsplashclient.R
import com.l3h4i15.unsplashclient.application.UnsplashApp
import com.l3h4i15.unsplashclient.databinding.FragmentRandomPictureBinding
import com.l3h4i15.unsplashclient.ui.main.MainViewModel
import com.l3h4i15.unsplashclient.ui.main.MainViewState
import com.l3h4i15.unsplashclient.ui.main.NavigationViewState
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

class RandomPictureFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    private lateinit var binding: FragmentRandomPictureBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_random_picture, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity().application as UnsplashApp).appComponent.inject(this)

        val activityViewModel by activityViewModels<MainViewModel> { factory }

        activityViewModel.setViewState(MainViewState.Random)
        binding.onClickLister = View.OnClickListener {
            activityViewModel.setNavigationState(NavigationViewState.COLLECTIONS)
        }

        val viewModel by viewModels<RandomPictureViewModel> { factory }
        binding.viewModel = viewModel

        viewModel.messageObservable
            .filter { it }
            .subscribe {
                Snackbar.make(view, R.string.loading_error_message, Snackbar.LENGTH_LONG)
                    .setAction(R.string.try_again_action) { viewModel.onLoadAction() }
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
        @BindingAdapter("randomUrl")
        fun setUrl(view: ImageView, url: String?) {
            Glide.with(view).load(url).into(view)
        }
    }
}