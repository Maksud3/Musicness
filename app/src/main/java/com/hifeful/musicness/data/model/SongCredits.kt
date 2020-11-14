package com.hifeful.musicness.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SongCredits(
    @SerializedName("release_date_for_display") @Expose val releaseDate: String,
    @SerializedName("primary_artist") @Expose val primaryArtist: Artist,
    @SerializedName("featured_artists") @Expose val featuredArtists: List<Artist>,
    @SerializedName("producer_artists") @Expose val producerArtists: List<Artist>,
    @SerializedName("writer_artists") @Expose val writerArtists: List<Artist>
) : Parcelable