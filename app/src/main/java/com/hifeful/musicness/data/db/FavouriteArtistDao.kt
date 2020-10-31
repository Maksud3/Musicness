package com.hifeful.musicness.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hifeful.musicness.data.model.Artist
import java.util.*

@Dao
interface FavouriteArtistDao {
    @Query("SELECT * FROM favourite_artist WHERE is_favourite = 1 ORDER BY timestamp DESC")
    suspend fun getFavouriteArtists(): List<Artist>
    @Query("SELECT EXISTS(SELECT * FROM favourite_artist WHERE id = :id)")
    suspend fun isFavouriteArtistExist(id: Long): Boolean
    @Query("SELECT EXISTS(SELECT * FROM favourite_artist WHERE id = :id AND is_favourite = 1)")
    suspend fun isArtistFavourite(id: Long): Boolean
    @Insert
    suspend fun insertFavouriteArtist(artist: Artist): Long

    @Query("UPDATE favourite_artist SET is_favourite = :isFavourite WHERE id = :id")
    suspend fun updateFavouriteArtist(id: Long, isFavourite: Boolean)
    @Query("UPDATE favourite_artist SET is_favourite = :isFavourite, timestamp = :timestamp WHERE id = :id")
    suspend fun updateFavouriteArtistWithTimestamp(id: Long, isFavourite: Boolean, timestamp: Date)
}