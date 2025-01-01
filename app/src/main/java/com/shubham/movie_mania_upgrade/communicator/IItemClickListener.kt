package com.shubham.movie_mania_upgrade.communicator

import com.shubham.movie_mania_upgrade.data.Search

interface IItemClickListener {
    fun onItemClicked(data : Search?)
}