package com.hifeful.musicness.ui.base

import android.widget.ImageView
import androidx.fragment.app.Fragment
import moxy.MvpView
import moxy.viewstate.strategy.SingleStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(SingleStateStrategy::class)
interface BaseView : MvpView {
    fun showDisplayHomeUp()
    fun showImage(fragment: Fragment, image: String, imageView: ImageView)
    fun showImage(fragment: Fragment, imageId: Int, imageView: ImageView)
}