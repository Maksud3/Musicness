package com.hifeful.musicness.ui.base

import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import moxy.MvpAppCompatFragment

abstract class BaseFragment : MvpAppCompatFragment(), BaseView {
    override fun showDisplayHomeUp() {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun showImage(fragment: Fragment, image: String, imageView: ImageView) {
        Glide.with(fragment)
            .load(image)
            .override(600)
            .into(imageView)
    }
}