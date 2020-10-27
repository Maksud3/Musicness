package com.hifeful.musicness.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Artist(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("image_url") val image_url: String
) : Parcelable