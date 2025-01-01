package com.shubham.movie_mania_upgrade.ui.movie

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shubham.movie_mania_upgrade.data.Search
import com.shubham.movie_mania_upgrade.remote.MovieRepository

class MoviePaging(private val searchString: String, private val movieApiRepo: MovieRepository) :
    PagingSource<Int, Search>() {

    override fun getRefreshKey(state: PagingState<Int, Search>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Search> {
        val page = params.key ?: 1
        return try {
            val response = movieApiRepo.getMovieData(
                userSearchedMovie = searchString,
                requiredPageNumber = page
            )
            val totalResults = response.body()?.totalResults?.toIntOrNull() ?: 0
            val currentData = response.body()?.search ?: emptyList()

            // Determine if there are remaining results
            val remainingResults = totalResults - (page * 10)

            // Stop pagination if no more data is available
            val nextKey = if (currentData.isEmpty() || remainingResults <= 0) null else page + 1

            LoadResult.Page(
                data = currentData,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }

}