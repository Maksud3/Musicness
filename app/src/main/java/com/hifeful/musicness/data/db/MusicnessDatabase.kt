package com.hifeful.musicness.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hifeful.musicness.App
import com.hifeful.musicness.data.model.Artist
import com.hifeful.musicness.util.DateConverter

@Database(entities = [Artist::class], version = 2)
@TypeConverters(DateConverter::class)
abstract class MusicnessDatabase : RoomDatabase() {
    abstract fun getFavouriteArtistDao(): FavouriteArtistDao

    companion object {
        private const val DATABASE_NAME = "musicness_db.db"
        private var instance: MusicnessDatabase? = null

        fun getInstance(): MusicnessDatabase {
            return instance ?: Room.databaseBuilder(App.getContext(),
                MusicnessDatabase::class.java,
                DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}