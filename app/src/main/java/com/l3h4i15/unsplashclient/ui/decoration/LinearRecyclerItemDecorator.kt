package com.l3h4i15.unsplashclient.ui.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.l3h4i15.unsplashclient.R
import javax.inject.Inject

class LinearRecyclerItemDecorator @Inject constructor() : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val dimension = view.resources.getDimensionPixelSize(R.dimen.recycler_item_distance)

        outRect.set(dimension, 0, dimension, dimension)
        if (parent.getChildAdapterPosition(view) == 0) outRect.top = dimension
    }
}