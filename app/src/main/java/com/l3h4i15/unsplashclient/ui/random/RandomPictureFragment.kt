package com.l3h4i15.unsplashclient.ui.random

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.l3h4i15.unsplashclient.R
import com.l3h4i15.unsplashclient.databinding.FragmentRandomPictureBinding
import androidx.fragment.app.viewModels
import com.l3h4i15.unsplashclient.ui.base.BaseFragment
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

class RandomPictureFragment @Inject constructor() : BaseFragment() {
    override val viewModel by viewModels<RandomPictureViewModel> { factory }

    override val title: Int
        get() = R.string.random_picture_title

    private lateinit var binding: FragmentRandomPictureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) viewModel.onLoadAction()
    }

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

        binding.viewModel = viewModel
        binding.onClickLister = View.OnClickListener { viewModel.navigate(navigation) }

        viewModel.messageObservable
            .subscribe {
                Snackbar.make(view, R.string.loading_error_message, Snackbar.LENGTH_LONG)
                    .setAction(R.string.try_again_action) { viewModel.onLoadAction() }
                    .show()
            }.addTo(compositeDisposable)
    }

    companion object {
        @JvmStatic
        @BindingAdapter("randomUrl")
        fun setUrl(view: ImageView, url: String?) {
            Glide.with(view).load(url).into(view)
        }
    }
}