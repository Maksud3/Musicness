package com.hifeful.musicness.util

import androidx.room.TypeConverter
import java.util.*

object DateConverter {
    private val TAG = DateConverter::class.qualifiedName

    @TypeConverter
    @JvmStatic
    fun toDate(dateLong: Long?): Date? {
        return if (dateLong == null) null else Date(dateLong)
    }

    @TypeConverter
    @JvmStatic
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}