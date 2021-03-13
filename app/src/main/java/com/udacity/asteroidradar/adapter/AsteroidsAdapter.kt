package com.udacity.asteroidradar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.ItemNeoBinding
import com.udacity.asteroidradar.model.Asteroid

class AsteroidsAdapter(private val clickListener: NeoListener) : ListAdapter<Asteroid,
        AsteroidsAdapter.Asteroids>(NeoCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Asteroids {
        return Asteroids.from(parent)
    }

    override fun onBindViewHolder(holder: Asteroids, position: Int) {
        val asteroids = getItem(position)
        holder.bind(clickListener, asteroids)
    }

    class Asteroids private constructor(private val binding: ItemNeoBinding):
            RecyclerView.ViewHolder(binding.root){
        fun bind(clickListener: NeoListener, asteroid: Asteroid) {
            binding.asteroid = asteroid
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): Asteroids {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemNeoBinding.inflate(layoutInflater, parent, false)
                return Asteroids(binding)
            }
        }
    }
}

class NeoCallBack : DiffUtil.ItemCallback<Asteroid>() {
    override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem == newItem
    }
}

class NeoListener(val clickListener: (asteroid: Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}