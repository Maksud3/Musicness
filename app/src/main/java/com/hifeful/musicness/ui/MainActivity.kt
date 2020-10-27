package com.hifeful.musicness.ui

import android.os.Bundle
import com.hifeful.musicness.R
import moxy.MvpAppCompatActivity
import moxy.MvpView
import moxy.ktx.moxyPresenter


class MainActivity : MvpAppCompatActivity(), MvpView {
    // Variables
    private val mPresenter by moxyPresenter { MainPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}