package com.hifeful.musicness.data.network

import com.hifeful.musicness.util.API_KEY
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GeniusClient {
    private const val BASE_URL = "https://api.genius.com/"
    private val client = OkHttpClient.Builder().addInterceptor {
        val newRequest = it.request().newBuilder()
            .addHeader("Authorization", "Bearer $API_KEY")
            .build()
        it.proceed(newRequest)
    }.build()

    private fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getGeniusClient(): GeniusService = getRetrofitInstance().create(GeniusService::class.java)
}