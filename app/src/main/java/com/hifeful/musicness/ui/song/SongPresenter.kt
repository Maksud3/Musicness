package com.hifeful.musicness.ui.song

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.hifeful.musicness.data.model.SongCredits
import com.hifeful.musicness.ui.base.BasePresenter
import com.hifeful.musicness.util.USER_AGENT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class SongPresenter : BasePresenter<SongView>() {
    private val TAG = SongPresenter::class.qualifiedName

    fun getSongLyrics(url: String) {
        launch {
            try {
                val doc = withContext(Dispatchers.IO) {
                    Jsoup.connect(url)
                        .userAgent(USER_AGENT)
                        .get()
                }
                val lyricsDiv = doc.select(".lyrics")
                if (lyricsDiv.isNotEmpty()) {
                    val lyrics = Jsoup.clean(lyricsDiv.html(),
                        Whitelist.none().addTags("br"))
                        .trim().replace("<br> ", "")
                    withContext(Dispatchers.Main) {
                        viewState.showSongLyrics(lyrics)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getSongCredits(id: Long) {
        mGeniusClient.getSongById(id).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val jsonSong = response.body()
                    ?.asJsonObject?.get("response")
                    ?.asJsonObject?.get("song")

                val songCredits = Gson().fromJson(jsonSong, SongCredits::class.java)
                if (songCredits != null) {
                    viewState.setUpSongCredits(songCredits)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                t.message?.let { Log.d(TAG, "Fail") }
            }
        })
    }
}