package com.hifeful.musicness.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Song(
    @SerializedName("id") @Expose val id: Long,
    @SerializedName("song_art_image_thumbnail_url") @Expose val image: String,
    @SerializedName("title_with_featured") @Expose val title: String,
    var primary_artist: String
)