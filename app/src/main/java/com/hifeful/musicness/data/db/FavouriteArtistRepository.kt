package com.hifeful.musicness.data.db

import com.hifeful.musicness.data.model.Artist
import java.util.*

class FavouriteArtistRepository(private val appDatabase: MusicnessDatabase) {
    suspend fun getFavouriteArtists() = appDatabase.getFavouriteArtistDao()
        .getFavouriteArtists()
    suspend fun isFavouriteArtistExist(id: Long) = appDatabase.getFavouriteArtistDao().isFavouriteArtistExist(id)
    suspend fun isArtistFavourite(id: Long) = appDatabase.getFavouriteArtistDao().isArtistFavourite(id)
    suspend fun insertFavouriteArtist(artist: Artist) = appDatabase.getFavouriteArtistDao()
        .insertFavouriteArtist(artist)
    suspend fun updateFavouriteArtist(id: Long, isFavourite: Boolean) = appDatabase.getFavouriteArtistDao()
        .updateFavouriteArtist(id, isFavourite)
    suspend fun updateFavouriteArtist(id: Long, isFavourite: Boolean, timestamp: Date) = appDatabase.getFavouriteArtistDao()
        .updateFavouriteArtistWithTimestamp(id, isFavourite, timestamp)
}