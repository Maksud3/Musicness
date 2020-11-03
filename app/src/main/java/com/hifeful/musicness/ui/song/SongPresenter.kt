package com.hifeful.musicness.ui.song

import android.util.Log
import com.hifeful.musicness.ui.base.BasePresenter
import com.hifeful.musicness.util.USER_AGENT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist
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
                if (lyricsDiv.isEmpty()) {
                    Log.i(TAG, "getSongLyrics: An error")
                } else {
                    val lyrics = Jsoup.clean(lyricsDiv.html(),
                        Whitelist.none().addTags("br"))
                        .trim().replace("<br> ", "")
                    Log.i(TAG, "onSongClick: $lyrics")
                    withContext(Dispatchers.Main) {
                        viewState.showSongLyrics(lyrics)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}