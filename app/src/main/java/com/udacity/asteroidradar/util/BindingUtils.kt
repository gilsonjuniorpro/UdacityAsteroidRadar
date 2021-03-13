package com.udacity.asteroidradar.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.dto.InfoDto
import com.udacity.asteroidradar.model.Asteroid

@BindingAdapter("asteroidName")
fun TextView.setAsteroidName(asteroid: Asteroid?) {
    asteroid?.let {
        text = asteroid.codename
    }
}

@BindingAdapter("asteroidDate")
fun TextView.setAsteroidDate(asteroid: Asteroid?) {
    asteroid?.let {
        text = asteroid.closeApproachDate
    }
}

@BindingAdapter("asteroidImage")
fun ImageView.setAsteroidImage(asteroid: Asteroid?) {
    if(asteroid?.isPotentiallyHazardous!!){
        setImageResource(
            R.drawable.ic_meteor_danger
        )
    }else{
        setImageResource(
            R.drawable.ic_meteor_not_danger
        )
    }
}

@BindingAdapter("itemInfo")
fun TextView.setItemInfo(infoDto: InfoDto?) {
    infoDto?.let {
        text = infoDto.item
    }
}

@BindingAdapter("descriptionInfo")
fun TextView.setDescriptionInfo(infoDto: InfoDto?) {
    infoDto?.let {
        text = infoDto.description
    }
}

@BindingAdapter("infoImage")
fun ImageView.setInfoImage(infoDto: InfoDto?) {
    visibility = if(infoDto?.item == "astronomical"){
        View.VISIBLE
    }else{
        View.GONE
    }
}

