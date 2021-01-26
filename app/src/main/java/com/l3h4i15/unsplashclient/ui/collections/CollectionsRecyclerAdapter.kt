package com.l3h4i15.unsplashclient.ui.collections

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.l3h4i15.unsplashclient.R
import com.l3h4i15.unsplashclient.databinding.ViewCollectionBinding
import com.l3h4i15.unsplashclient.listener.OnCollectionClick
import com.l3h4i15.unsplashclient.model.Collection
import javax.inject.Inject

class CollectionsRecyclerAdapter @Inject constructor() :
    RecyclerView.Adapter<CollectionsRecyclerAdapter.ViewHolder>() {
    private val _collections: MutableList<Collection> = mutableListOf()
    var collections: List<Collection>
        get() = _collections
        set(value) {
            _collections.clear()
            _collections.addAll(value)
        }

    private var onClick: OnCollectionClick = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewCollectionBinding>(
                inflater, R.layout.view_collection, parent, false
            )

        val holder = ViewHolder(binding)

        binding.root.setOnClickListener { onClick(collections[holder.layoutPosition]) }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.collection = collections[position]
    }

    override fun getItemCount() = collections.size

    fun setOnCollectionClickListener(onCollectionClick: OnCollectionClick) {
        onClick = onCollectionClick
    }

    class ViewHolder(val binding: ViewCollectionBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            @JvmStatic
            @BindingAdapter("collectionUrl")
            fun setUrl(view: ImageView, url: String?) {
                Glide.with(view).load(url).centerCrop().placeholder(R.drawable.ic_image_placeholder)
                    .into(view)
            }
        }
    }
}