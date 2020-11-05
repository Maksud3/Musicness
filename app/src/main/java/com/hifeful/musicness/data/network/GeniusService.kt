package com.hifeful.musicness.data.network

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GeniusService {
    @GET("artists/{id}")
    fun getArtistById(@Path("id") id: Long,
                      @Query("text_format") textFormat: String = "dom"): Call<JsonObject>
    @GET("artists/{id}/songs")
    fun getArtistPopularSongs(@Path("id") id: Long,
                              @Query("sort") sort: String = "popularity",
                              @Query("per_page") songs: Long = 10) : Call<JsonObject>
    @GET("search")
    fun searchSongs(@Query("q") query: String) : Call<JsonObject>
}