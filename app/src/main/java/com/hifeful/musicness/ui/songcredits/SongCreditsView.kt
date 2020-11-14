package com.hifeful.musicness.ui.songcredits

import android.view.View
import com.hifeful.musicness.data.model.Artist
import com.hifeful.musicness.ui.base.BaseView
import moxy.viewstate.strategy.SingleStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(SingleStateStrategy::class)
interface SongCreditsView : BaseView {
    fun setUpToolbar(view: View)
    fun setUpSongCredits()
    fun setUpPrimaryArtist()
    fun setUpStaff(staffList: List<Artist>, headerId: Int, recyclerId: Int, label: String)
}