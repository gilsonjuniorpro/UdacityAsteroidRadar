package com.udacity.asteroidradar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.dto.InfoDto
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.repository.NeoRepository

class DetailViewModel(
    private val repository: NeoRepository
) : ViewModel() {
    private val _list = MutableLiveData<List<InfoDto>>()
    val list: LiveData<List<InfoDto>> = _list

    fun extractList(neoDto: Asteroid?) {
        val listItems = mutableListOf<InfoDto>()

        listItems.add(
            InfoDto(
              "estimated diameter",
                neoDto?.estimatedDiameter.toString()
            )
        )

        listItems.add(
                InfoDto(
                        "absolute magnitude",
                        neoDto?.absoluteMagnitude.toString()
                )
        )

        listItems.add(
                InfoDto(
                        "kilometers per second",
                        neoDto?.relativeVelocity.toString()
                )
        )

        listItems.add(
                InfoDto(
                        "astronomical",
                        neoDto?.distanceFromEarth.toString()
                )
        )

        _list.value = listItems
    }
}