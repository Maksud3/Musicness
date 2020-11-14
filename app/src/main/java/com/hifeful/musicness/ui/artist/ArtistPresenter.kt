package com.hifeful.musicness.ui.artist

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.hifeful.musicness.data.model.Song
import com.hifeful.musicness.data.network.GeniusClient
import com.hifeful.musicness.ui.base.BasePresenter
import com.hifeful.musicness.util.API_KEY
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


class ArtistPresenter : BasePresenter<ArtistView>() {
    private val TAG = ArtistPresenter::class.qualifiedName
    
    fun getArtistPopularSongs(id: Long) {
        mGeniusClient.getArtistPopularSongs(id).enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                val jsonSongArray = response.body()
                    ?.asJsonObject?.get("response")
                    ?.asJsonObject?.getAsJsonArray("songs")

                val gson = GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create()

                val songs = mutableListOf<Song>()
                for (i in 0 until (jsonSongArray?.size() ?: 0)) {
                    val currentSong = jsonSongArray?.get(i)
                    val song = gson.fromJson(currentSong, Song::class.java)
                    val artistName = currentSong?.asJsonObject?.get("primary_artist")
                        ?.asJsonObject?.get("name")?.asString
                    song.primary_artist = artistName.toString()
                    songs.add(song)
                }
                viewState.showArtistPopularSongs(songs)
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                t.message?.let { Log.d("MainActivity", "Fail") }
            }
        })
    }

    fun isFavouriteArtistExist(id: Long) {
        launch {
            val isExist = mFavouriteArtistRepository.isFavouriteArtistExist(id)
            viewState.isFavouriteArtistCached(isExist)
            if (isExist) {
                withContext(Dispatchers.Main) {
                    viewState.initFavouriteButton(mFavouriteArtistRepository.isArtistFavourite(id))
                }
            }
        }
    }
}