package com.hifeful.musicness.util

import android.util.Log
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val TAG = DateUtils::class.qualifiedName
    fun getCurrentTimestamp(): String? {
        return try {
            val dateFormat = SimpleDateFormat("MMM-yyyy", Locale.US)
            dateFormat.format(Date())
        } catch (e: Exception) {
            Log.i(TAG, "getCurrentTimestamp: ${e.message}")
            null
        }
    }
}