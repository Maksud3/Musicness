package com.hifeful.musicness.ui.home

import com.hifeful.musicness.data.model.Artist
import com.hifeful.musicness.ui.base.BaseView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface HomeView : BaseView {
    fun setUpArtistRecycler()
    fun showArtist(artist: Artist)
}