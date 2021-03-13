package com.udacity.asteroidradar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.ItemDetailBinding
import com.udacity.asteroidradar.dto.InfoDto

class DetailsAdapter(private val clickListener: DetailListener) : ListAdapter<InfoDto,
        DetailsAdapter.Info>(DetailCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Info {
        return Info.from(parent)
    }

    override fun onBindViewHolder(holder: Info, position: Int) {
        val infoDto = getItem(position)
        holder.bind(clickListener, infoDto)
    }

    class Info private constructor(private val binding: ItemDetailBinding):
            RecyclerView.ViewHolder(binding.root){
        fun bind(clickListener: DetailListener, infoDto: InfoDto) {
            binding.infoDto = infoDto
            if(infoDto.item == "astronomical"){
                binding.clickListener = clickListener
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): Info {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemDetailBinding.inflate(layoutInflater, parent, false)
                return Info(binding)
            }
        }
    }
}

class DetailCallBack : DiffUtil.ItemCallback<InfoDto>() {
    override fun areItemsTheSame(oldItem: InfoDto, newItem: InfoDto): Boolean {
        return oldItem.item == newItem.item
    }

    override fun areContentsTheSame(oldItem: InfoDto, newItem: InfoDto): Boolean {
        return oldItem == newItem
    }
}

class DetailListener(val clickListener: (infoDto: InfoDto) -> Unit) {
    fun onClick(infoDto: InfoDto) = clickListener(infoDto)
}