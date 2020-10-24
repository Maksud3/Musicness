package com.hifeful.musicness.ui.artist

import com.hifeful.musicness.data.model.Artist

interface ArtistContract {
    interface View {
        fun setUpToolbar(view: android.view.View)
        fun showDisplayHomeUp()
        fun setUpCollapsingToolbar()
        fun showArtistImage()
        fun showArtistDetails(artist: Artist)
    }
    interface Presenter {
        fun getArtistById(id: Long)
    }
}