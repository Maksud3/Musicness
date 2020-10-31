package com.hifeful.musicness.util

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacesItemDecoration(private val leftSpace: Int, private val topSpace: Int,
                           private val rightSpace: Int, private val bottomSpace: Int) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(leftSpace, topSpace, rightSpace, bottomSpace)
    }
}