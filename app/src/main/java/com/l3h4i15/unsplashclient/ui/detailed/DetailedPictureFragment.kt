package com.l3h4i15.unsplashclient.ui.detailed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.l3h4i15.unsplashclient.R
import com.l3h4i15.unsplashclient.databinding.FragmentDetailedPictureBinding
import com.l3h4i15.unsplashclient.navigation.Navigation
import javax.inject.Inject

class DetailedPictureFragment @Inject constructor(private val navigation: Navigation) : Fragment() {

    private lateinit var binding: FragmentDetailedPictureBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_detailed_picture, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigation.setTitle(R.string.detailed_picture_title)

        binding.picture = arguments?.getParcelable(PICTURE) ?: throw IllegalStateException()

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

    companion object {
        const val PICTURE = "picture"

        @JvmStatic
        @BindingAdapter("detailedUrl")
        fun setUrl(view: ImageView, url: String?) {
            Glide.with(view).load(url).into(view)
        }
    }
}