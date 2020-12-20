package com.l3h4i15.unsplashclient.util.diff

import androidx.recyclerview.widget.DiffUtil
import com.l3h4i15.unsplashclient.model.content.Collection

class CollectionsDiffUtilCallback(
    private val oldList: List<Collection>,
    private val newList: List<Collection>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size
}