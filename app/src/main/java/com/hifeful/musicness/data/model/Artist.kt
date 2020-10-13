package com.hifeful.musicness.data.model

import com.google.gson.annotations.SerializedName

data class Artist(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("image_url") val image_url: String
)