package com.hifeful.musicness.ui.home

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.hifeful.musicness.data.model.Artist
import com.hifeful.musicness.data.model.Song
import com.hifeful.musicness.data.network.GeniusClient
import com.hifeful.musicness.ui.base.BasePresenter
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class HomePresenter : BasePresenter<HomeView>() {
    private val TAG = HomePresenter::class.qualifiedName
    var mArtists: MutableList<Artist>? = null

    private var queryTextChangedJob: Job? = null

    // SearchView
    var mIsSearchViewVisible = false
    var mSongsSearchView: List<Song> = listOf()
    var mSongNamesSearchView: Array<String> = mutableListOf<String>().toTypedArray()

    private fun getArtistById(id: Long) {
        mGeniusClient.getArtistById(id).enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                val jsonArtist = response.body()
                    ?.asJsonObject?.get("response")
                    ?.asJsonObject?.get("artist")

                val artist = Gson().fromJson(jsonArtist, Artist::class.java)
                if (artist != null) {
                    mArtists?.add(artist)
                    viewState.showArtist(artist)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                t.message?.let { Log.d("MainActivity", "Fail") }
            }
        })
    }

    private fun searchSongs(query: String) {
        mGeniusClient.searchSongs(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val jsonSongs = response.body()
                    ?.asJsonObject?.get("response")
                    ?.asJsonObject?.getAsJsonArray("hits")

                val gson = GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create()

                val songs = mutableListOf<Song>()

                for (i in 0 until (jsonSongs?.size() ?: 0)) {
                    val currentJsonSong = jsonSongs?.get(i)?.asJsonObject?.get("result")
                    val currentSong = gson.fromJson(currentJsonSong, Song::class.java)
                    val artistName = currentJsonSong?.asJsonObject?.get("primary_artist")
                        ?.asJsonObject?.get("name")?.asString
                    currentSong.primary_artist = artistName.toString()
                    songs.add(currentSong)
                }
                viewState.showSearchResults(songs)
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                t.message?.let { Log.d("MainActivity", "Fail") }
            }
        })
    }

    fun searchSongsThrottled(query: String) {
        queryTextChangedJob?.cancel()

        queryTextChangedJob = launch(Dispatchers.IO) {
            delay(500)
            Log.i(TAG, "searchSongsThrottled: after delay")
            searchSongs(query)
        }
    }

    fun getFavoriteArtists() {
        launch {
            val artists = mFavouriteArtistRepository.getFavouriteArtists()
            withContext(Dispatchers.Main) {
                viewState.showFavoriteArtists(artists)
            }
        }
    }

    fun getRandomArtists(amount: Int = 30) {
        if (mArtists != null && mArtists!!.isNotEmpty()) {
            Log.i(TAG, "getRandomArtists: show")
            viewState.showArtists(mArtists!!)
        } else {
            Log.i(TAG, "getRandomArtists: fetch")
            mArtists = mutableListOf()
            repeat(amount) {
                getArtistById(Random.nextLong(1, 10000))
            }
        }
    }
}