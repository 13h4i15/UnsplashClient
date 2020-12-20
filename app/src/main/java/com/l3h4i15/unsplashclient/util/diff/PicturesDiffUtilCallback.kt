package com.l3h4i15.unsplashclient.util.diff

import androidx.recyclerview.widget.DiffUtil
import com.l3h4i15.unsplashclient.model.content.Picture

class PicturesDiffUtilCallback(
    private val oldList: List<Picture>,
    private val newList: List<Picture>
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