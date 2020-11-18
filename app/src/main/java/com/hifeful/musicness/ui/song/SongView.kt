package com.hifeful.musicness.ui.song

import com.hifeful.musicness.data.model.SongCredits
import com.hifeful.musicness.ui.base.BaseView
import moxy.viewstate.strategy.*
import moxy.viewstate.strategy.alias.AddToEnd
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.SingleState

@StateStrategyType(SingleStateStrategy::class)
interface SongView : BaseView {
    fun setUpToolbar(view: android.view.View)
    fun setUpCollapsingToolbar()
    @StateStrategyType(value = AddToEndSingleTagStrategy::class, tag = "show_hide_toolbar_info")
    fun showToolbarInfo()
    @StateStrategyType(value = AddToEndSingleTagStrategy::class, tag = "show_hide_toolbar_info")
    fun hideToolbarInfo()
    @AddToEnd
    fun setUpSongCredits(songCredits: SongCredits)
    fun showSongDetails()
    fun showSongLyrics(lyrics: String)
}