package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.model.Apod
import com.udacity.asteroidradar.model.Neo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NeoService {
    @GET("planetary/apod")
    fun getApod(
            @Query("api_key") apiKey: String,
            @Query("date") date: String?
    ): Call<Apod>

    @GET("neo/rest/v1/feed")
    fun getNeoFeedCall(
        @Query("api_key") apiKey: String,
        @Query("start_date") startDate: String?,
        @Query("end_date") endDate: String?
    ): Call<Neo>

    @GET("neo/rest/v1/feed")
    fun getNeoFeed(
        @Query("api_key") apiKey: String,
        @Query("start_date") startDate: String?,
        @Query("end_date") endDate: String?
    ): Call<Neo>
}