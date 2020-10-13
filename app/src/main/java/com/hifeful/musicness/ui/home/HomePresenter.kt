package com.hifeful.musicness.ui.home

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.hifeful.musicness.data.model.Artist
import com.hifeful.musicness.data.network.GeniusClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class HomePresenter(private val mView: HomeContract.View) : HomeContract.Presenter {
    private val mGeniusClient = GeniusClient.getGeniusClient()

    override fun getArtistById(id: Long) {
        mGeniusClient.getArtistById(id).enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                val jsonArtist = response.body()
                    ?.asJsonObject?.get("response")
                    ?.asJsonObject?.get("artist")

                val artist = Gson().fromJson(jsonArtist, Artist::class.java)
                if (artist != null) mView.showArtist(artist)
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                t.message?.let { Log.d("MainActivity", "Fail") }
            }
        })
    }

    override fun getRandomArtists(amount: Int) {
        repeat(amount) {
            getArtistById(Random.nextLong(1, 10000))
        }
    }
}