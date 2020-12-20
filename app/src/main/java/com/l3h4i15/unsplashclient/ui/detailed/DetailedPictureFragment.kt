package com.l3h4i15.unsplashclient.ui.detailed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.l3h4i15.unsplashclient.R
import com.l3h4i15.unsplashclient.application.UnsplashApp
import com.l3h4i15.unsplashclient.databinding.FragmentDetailedPictureBinding
import com.l3h4i15.unsplashclient.model.content.Picture
import com.l3h4i15.unsplashclient.ui.main.MainViewModel
import com.l3h4i15.unsplashclient.ui.main.MainViewState
import javax.inject.Inject

class DetailedPictureFragment : Fragment() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var picture: Picture

    private val unsplashApp: UnsplashApp by lazy { requireActivity().application as UnsplashApp }

    private lateinit var binding: FragmentDetailedPictureBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_detailed_picture, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val component = unsplashApp.detailedPictureComponent ?: return
        component.inject(this)

        val activityViewModel by activityViewModels<MainViewModel> { factory }
        activityViewModel.setViewState(MainViewState.Detailed)

        binding.picture = picture

        val sheetBehavior = BottomSheetBehavior.from(binding.contentLayout)
        sheetBehavior.isFitToContents = true
        sheetBehavior.isHideable = true
        sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        binding.onClickListener = View.OnClickListener {
            sheetBehavior.state = if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                BottomSheetBehavior.STATE_HIDDEN
            } else {
                BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    override fun onDestroy() {
        if (!requireActivity().isChangingConfigurations) unsplashApp.freeDetailedPictureComponent()
        super.onDestroy()
    }

    companion object {
        @JvmStatic
        @BindingAdapter("detailedUrl")
        fun setUrl(view: ImageView, url: String?) {
            Glide.with(view).load(url).into(view)
        }
    }
}