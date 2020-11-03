package com.hifeful.musicness.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Song(
    @SerializedName("id") @Expose val id: Long,
    @SerializedName("song_art_image_url") @Expose val image: String,
    @SerializedName("title_with_featured") @Expose val title: String,
    @SerializedName("url") @Expose val url: String,
    var primary_artist: String
) : Parcelable