package com.hifeful.musicness.ui.home

import com.hifeful.musicness.data.model.Artist

interface HomeContract {
    interface View {
        fun showArtist(artist: Artist)
    }

    interface Presenter {
        fun getArtistById(id: Long)
        fun getRandomArtists(amount: Int)
    }
}