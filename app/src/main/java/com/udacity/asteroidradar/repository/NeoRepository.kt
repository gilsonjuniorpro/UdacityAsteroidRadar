package com.udacity.asteroidradar.repository

import android.content.Context
import com.udacity.asteroidradar.model.Apod
import com.udacity.asteroidradar.model.Asteroid

class NeoRepository(context: Context) {

    private val neoDao =
        AppDatabase.getDatabase(context).getNeoDao()

    suspend fun insertApod(apod: Apod) {
        neoDao.insertApod(apod)
    }

    suspend fun deleteAllApod() {
        neoDao.deleteAllApod()
    }

    suspend fun deleteAllNeo() {
        neoDao.deleteAllNeo()
    }

    suspend fun insertAll(asteroid: List<Asteroid>){
        neoDao.insertAll(asteroid)
    }

    suspend fun getApod(): Apod {
        return neoDao.getApod()
    }

    suspend fun getAllNeos(): List<Asteroid> {
        return neoDao.getAllNeos()
    }
}