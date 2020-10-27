package com.hifeful.musicness.ui.base

import moxy.MvpPresenter

abstract class BasePresenter<V : BaseView> : MvpPresenter<V>() {
}