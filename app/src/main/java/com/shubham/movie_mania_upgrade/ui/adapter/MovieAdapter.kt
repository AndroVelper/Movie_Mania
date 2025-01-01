package com.shubham.movie_mania_upgrade.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shubham.movie_mania_upgrade.BR
import com.shubham.movie_mania_upgrade.communicator.IItemClickListener
import com.shubham.movie_mania_upgrade.data.Search
import com.shubham.movie_mania_upgrade.databinding.ItemMovieBinding

class MovieAdapter(private val listener : IItemClickListener):
    PagingDataAdapter<Search, MovieAdapter.MyViewHolderClass>(MovieAdapter.DIFF_UTIL) {

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Search>() {
            override fun areItemsTheSame(oldItem: Search, newItem: Search): Boolean {
                return oldItem.imdbID == newItem.imdbID
            }

            override fun areContentsTheSame(oldItem: Search, newItem: Search): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onBindViewHolder(holder: MyViewHolderClass, position: Int) {
        with(holder.binding) { setVariable(BR.data, getItem(position))
             root.setOnClickListener { listener.onItemClicked(getItem(position)) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderClass {
        return MyViewHolderClass(
            ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class MyViewHolderClass(val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root)
}