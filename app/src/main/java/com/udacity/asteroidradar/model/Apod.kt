package com.udacity.asteroidradar.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Apod(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val copyright: String?,
    val date: String?,
    val explanation: String?,
    val hdurl: String?,
    val media_type: String?,
    val service_version: String?,
    val title: String?,
    val url: String?
)