package com.udacity.asteroidradar.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.udacity.asteroidradar.model.Apod
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.util.DateConverter

@Database(entities = [Asteroid::class, Apod::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase() : RoomDatabase() {

    abstract fun getNeoDao(): NeoDao

    companion object {
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context) : AppDatabase{
            if(instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "neoDb"
                ).build()
            }
            return instance!!
        }
    }
}