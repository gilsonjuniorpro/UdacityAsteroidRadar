package com.udacity.asteroidradar.repository

import androidx.room.*
import com.udacity.asteroidradar.model.Apod
import com.udacity.asteroidradar.model.Asteroid

@Dao
interface NeoDao {
    @Query("DELETE FROM Apod")
    suspend fun deleteAllApod()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApod(apod: Apod)

    @Query("DELETE FROM Asteroid")
    suspend fun deleteAllNeo()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(asteroids: List<Asteroid>)

    @Query("SELECT * FROM Apod ORDER BY id DESC LIMIT 1")
    suspend fun getApod() : Apod

    @Query("SELECT * FROM Asteroid ORDER BY id DESC")
    suspend fun getAllNeos() : List<Asteroid>
}