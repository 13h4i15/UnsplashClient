package com.l3h4i15.unsplashclient.ui.collectionpictures

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.l3h4i15.unsplashclient.R
import com.l3h4i15.unsplashclient.databinding.ViewPictureBinding
import com.l3h4i15.unsplashclient.listener.OnPictureClick
import com.l3h4i15.unsplashclient.model.Picture
import javax.inject.Inject

class CollectionPicturesRecyclerAdapter @Inject constructor() :
    RecyclerView.Adapter<CollectionPicturesRecyclerAdapter.ViewHolder>() {
    private val _pictures: MutableList<Picture> = mutableListOf()
    var pictures: List<Picture>
        get() = _pictures
        set(value) {
            _pictures.clear()
            _pictures.addAll(value)
        }

    private var onClick: OnPictureClick = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewPictureBinding>(
            inflater, R.layout.view_picture, parent, false
        )
        val holder = ViewHolder(binding)

        binding.root.setOnClickListener {
            onClick(pictures[holder.layoutPosition])
        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.picture = pictures[position]
    }

    override fun getItemCount(): Int = pictures.size

    fun setOnPictureClickListener(onPictureClick: OnPictureClick) {
        onClick = onPictureClick
    }

    class ViewHolder(val binding: ViewPictureBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            @JvmStatic
            @BindingAdapter("collectionPictureUrl")
            fun setPreview(view: ImageView, url: String?) {
                Glide.with(view).load(url).centerCrop().placeholder(R.drawable.ic_image_placeholder)
                    .into(view)
            }
        }
    }
}