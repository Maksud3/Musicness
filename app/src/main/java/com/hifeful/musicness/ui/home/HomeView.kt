package com.hifeful.musicness.ui.home

import com.hifeful.musicness.data.model.Artist
import com.hifeful.musicness.ui.base.BaseView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.SingleStateStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(SingleStateStrategy::class)
interface HomeView : BaseView {
    fun setUpFavoriteSection()
    @StateStrategyType(AddToEndStrategy::class)
    fun showFavoriteArtists(artists: List<Artist>)
    fun setUpArtistRecycler()
    @StateStrategyType(AddToEndStrategy::class)
    fun showArtist(artist: Artist)
    fun showArtists(artists: List<Artist>)
}