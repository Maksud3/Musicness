package com.hifeful.musicness.ui.artist

import com.hifeful.musicness.data.model.Artist

interface ArtistContract {
    interface View {
        fun setUpArtistDetails(artist: Artist)
    }
    interface Presenter {
        fun getArtistById(id: Long)
    }
}