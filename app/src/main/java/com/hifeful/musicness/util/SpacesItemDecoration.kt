package com.hifeful.musicness.util

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacesItemDecoration : RecyclerView.ItemDecoration {
    private val mSpace: Int
    constructor(space: Int) {
        mSpace = space
    }

    constructor(context: Context, spaceId: Int)
            : this(context.resources.getDimensionPixelSize(spaceId)) {
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(mSpace, mSpace, mSpace, mSpace)
    }
}