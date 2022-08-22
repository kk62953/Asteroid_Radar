package com.udacity.asteroidradar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidItemListBinding

class MainRecyclerAdapter(val clickListener: OnClickListener): ListAdapter<Asteroid, MainRecyclerAdapter.AsteroidViewHolder>(
    AsteroidAdapterDiffCallback()) {

    //Retrieve item from data list and display it in ViewHolder
    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {

        val asteroid = getItem(position)
        holder.bind(asteroid)
        holder.itemView.setOnClickListener {
           clickListener.onClick(asteroid)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder.from(parent)
    }

    class AsteroidViewHolder private constructor (val binding: AsteroidItemListBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind( asteroid: Asteroid) {
            binding.asteroid = asteroid
            if(asteroid.isPotentiallyHazardous){
                binding.asteroidItemHazardIcon.contentDescription = itemView.context.getString(R.string.potentially_hazardous_asteroid_icon)
            }else{
                binding.asteroidItemHazardIcon.contentDescription = itemView.context.getString(R.string.not_hazardous_asteroid_icon)
            }
            binding.executePendingBindings()
        }

        companion object {
            /* Function to encapsulate details of inflation and what Layout to viewHolder class*/
            fun from(parent: ViewGroup): AsteroidViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AsteroidItemListBinding.inflate(layoutInflater,parent,false)
                return AsteroidViewHolder(binding)
            }
        }
    }

    /*Helper class  for MainRecyclerAdapter class that calculate changes in list
 and minimize modifications*/
    class AsteroidAdapterDiffCallback :  DiffUtil.ItemCallback<Asteroid>(){
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id== newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem== newItem
        }
    }


    class OnClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }


}



