package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.model.Apod
import com.udacity.asteroidradar.model.Neo
import com.udacity.asteroidradar.util.Constants
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class NeoController {
    companion object {
        private val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()


        //using moshi I am facing this problem:
        //com.squareup.moshi.JsonDataException: Required value 'copyright' missing at $
        //but works with Gson
        private fun getRetrofit(): NeoService? {
            val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
                //.addConverterFactory(MoshiConverterFactory.create(moshi))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()

            return retrofit.create(NeoService::class.java)
        }

        fun getApodCall(date: String?): Call<Apod>? {
            val call = getRetrofit()?.getApod(Constants.API_KEY, date)

            return call
        }

        fun getNeoFeedCall(startDate: String?, endDate: String?): Call<Neo>? {
            val call = getRetrofit()?.getNeoFeedCall(Constants.API_KEY, startDate, endDate)

            return call
        }

        fun getNeoFeed(startDate: String?, endDate: String?): Call<Neo>? {
            val neo = getRetrofit()?.getNeoFeed(Constants.API_KEY, startDate, endDate)

            return neo
        }
    }
}