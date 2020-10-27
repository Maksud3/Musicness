package com.hifeful.musicness.ui.home

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.hifeful.musicness.data.model.Artist
import com.hifeful.musicness.data.network.GeniusClient
import com.hifeful.musicness.ui.base.BasePresenter
import moxy.InjectViewState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class HomePresenter : BasePresenter<HomeView>() {
    private val TAG = HomePresenter::class.qualifiedName
    
    private val mGeniusClient = GeniusClient.getGeniusClient()

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
                if (artist != null) viewState.showArtist(artist)
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                t.message?.let { Log.d("MainActivity", "Fail") }
            }
        })
    }

    fun getRandomArtists(amount: Int) {
        Log.i(TAG, "getRandomArtists: ")
        repeat(amount) {
            getArtistById(Random.nextLong(1, 10000))
        }
    }
}