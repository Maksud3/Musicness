package com.hifeful.musicness.ui.home

import android.view.MenuItem
import android.view.View
import com.hifeful.musicness.data.model.Artist
import com.hifeful.musicness.data.model.Song
import com.hifeful.musicness.ui.base.BaseView
import moxy.viewstate.strategy.SingleStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(SingleStateStrategy::class)
interface HomeView : BaseView {
    fun setUpToolbar(view: View)
    fun setUpSearch(searchItem: MenuItem)
    fun showSearchResults(songs: List<Song>)
    fun setUpFavoriteSection()
    fun showFavoriteArtists(artists: List<Artist>)
    fun setUpArtistRecycler()
    fun showArtist(artist: Artist)
    fun showArtists(artists: List<Artist>)
}