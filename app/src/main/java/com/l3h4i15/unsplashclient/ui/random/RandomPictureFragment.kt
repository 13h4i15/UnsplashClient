package com.l3h4i15.unsplashclient.ui.random

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.l3h4i15.unsplashclient.R
import com.l3h4i15.unsplashclient.databinding.FragmentRandomPictureBinding
import com.l3h4i15.unsplashclient.navigation.Navigation
import com.l3h4i15.unsplashclient.navigation.newRootFragment
import com.l3h4i15.unsplashclient.ui.collections.CollectionsFragment
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

class RandomPictureFragment @Inject constructor(
    private val factory: ViewModelProvider.Factory,
    private val compositeDisposable: CompositeDisposable,
    private val navigation: Navigation
) : Fragment() {
    private lateinit var binding: FragmentRandomPictureBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_random_picture, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigation.setTitle(R.string.random_picture_title)

        binding.onClickLister = View.OnClickListener {
            navigation.newRootFragment<CollectionsFragment>()
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