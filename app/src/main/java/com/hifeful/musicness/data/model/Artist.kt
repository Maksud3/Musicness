package com.hifeful.musicness.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "favourite_artist")
@Parcelize
data class Artist(
    @PrimaryKey @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("image_url") val image_url: String,
    @ColumnInfo(name = "is_favourite") var isFavourite: Boolean = false,
    var timestamp: String = ""
) : Parcelable