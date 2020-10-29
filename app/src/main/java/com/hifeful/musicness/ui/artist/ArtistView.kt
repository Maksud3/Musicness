package com.hifeful.musicness.ui.artist

import com.hifeful.musicness.data.model.Song
import com.hifeful.musicness.ui.base.BaseView
import moxy.viewstate.strategy.SingleStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(SingleStateStrategy::class)
interface ArtistView : BaseView {
    fun setUpToolbar(view: android.view.View)
    fun showDisplayHomeUp()
    fun setUpCollapsingToolbar()
    fun showArtistDetails()
    fun showArtistPopularSongs(songs: List<Song>)
    fun setUpSongRecycler()
    fun initFavouriteButton(isPressed: Boolean)
    fun isFavouriteArtistCached(isCached: Boolean)
    fun onFavouriteButtonClick()
    fun enableFavouriteButton()
    fun disableFavouriteButton()
    fun setUpFab()
}