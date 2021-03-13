package com.udacity.asteroidradar.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.repository.NeoRepository

class MainViewModelFactory(
    private var repository: NeoRepository,
    private var application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(repository, application) as T
        }
        throw IllegalArgumentException("Unkown ViewModel Class")
    }
}