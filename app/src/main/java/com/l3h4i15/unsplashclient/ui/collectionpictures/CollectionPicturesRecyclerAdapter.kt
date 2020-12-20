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
import com.l3h4i15.unsplashclient.listener.OnPictureClickListener
import com.l3h4i15.unsplashclient.model.content.Picture
import javax.inject.Inject

class CollectionPicturesRecyclerAdapter @Inject constructor() :
    RecyclerView.Adapter<CollectionPicturesRecyclerAdapter.ViewHolder>() {
    private val data: MutableList<Picture> = mutableListOf()
    var pictures: List<Picture>
        get() = data
        set(value) {
            data.clear()
            data.addAll(value)
        }

    private var clickListener: OnPictureClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewPictureBinding>(
            inflater, R.layout.view_picture, parent, false
        )
        val holder = ViewHolder(binding)

        binding.root.setOnClickListener {
            clickListener?.onClick(data[holder.layoutPosition])
        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.picture = data[position]
    }

    override fun getItemCount(): Int = data.size

    fun setOnPictureClickListener(clickListener: OnPictureClickListener) {
        this.clickListener = clickListener
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