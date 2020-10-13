package com.hifeful.musicness.ui

import android.os.Bundle
import com.hifeful.musicness.R
import com.hifeful.musicness.ui.base.BaseActivity


class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}