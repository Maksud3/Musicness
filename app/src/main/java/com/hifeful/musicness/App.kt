package com.hifeful.musicness

import android.app.Application
import android.content.Context

class App : Application() {
    init {
        mApplication = this
    }

    companion object {
        private var mApplication: Application? = null

        fun getContext(): Context = mApplication!!.applicationContext
    }
}