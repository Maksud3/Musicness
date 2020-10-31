package com.hifeful.musicness.ui.base

import android.util.Log
import com.hifeful.musicness.data.db.FavouriteArtistRepository
import com.hifeful.musicness.data.db.MusicnessDatabase
import com.hifeful.musicness.data.model.Artist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import java.util.*
import kotlin.coroutines.CoroutineContext

abstract class BasePresenter<V : BaseView> : MvpPresenter<V>(), CoroutineScope {
    private val TAG = BasePresenter::class.qualifiedName
    
    protected val mFavouriteArtistRepository = FavouriteArtistRepository(MusicnessDatabase.getInstance())

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    fun addFavouriteArtist(artist: Artist) {
        launch {
            val artistId = mFavouriteArtistRepository.insertFavouriteArtist(artist)
            Log.i(TAG, "addFavouriteArtist: $artistId")
        }
    }

    fun updateFavouriteArtist(id: Long, isFavourite: Boolean) {
        launch {
            mFavouriteArtistRepository.updateFavouriteArtist(id, isFavourite)
        }
    }

    fun updateFavouriteArtist(id: Long, isFavourite: Boolean, timestamp: Date) {
        launch {
            mFavouriteArtistRepository.updateFavouriteArtist(id, isFavourite, timestamp)
        }
    }
}