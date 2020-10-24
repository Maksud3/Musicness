package com.hifeful.musicness.ui.artist

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.hifeful.musicness.data.model.Artist
import com.hifeful.musicness.data.network.GeniusClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ArtistPresenter(private val mView: ArtistContract.View) : ArtistContract.Presenter {
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
                if (artist != null) mView.showArtistDetails(artist)
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                t.message?.let { Log.d("MainActivity", "Fail") }
            }
        })
    }

}