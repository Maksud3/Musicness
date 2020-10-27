package com.hifeful.musicness.ui.base

import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import moxy.MvpAppCompatFragment

abstract class BaseFragment : MvpAppCompatFragment(), BaseView {
    override fun showImage(fragment: Fragment, image: String, imageView: ImageView) {
        Glide.with(fragment)
            .load(image)
            .into(imageView)
    }
}