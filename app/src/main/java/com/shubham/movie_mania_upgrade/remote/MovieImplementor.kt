package com.shubham.movie_mania_upgrade.remote

import com.shubham.movie_mania_upgrade.data.MovieDetailResponse
import com.shubham.movie_mania_upgrade.data.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IMovieImplementor {

    @GET("/")
    suspend fun getMovieData(
        @Query("apikey") apikey :String = "c23f0fa4",
        @Query("s") searchMovie: String,
        @Query("page") pageNumber: Int,
    ): Response<MovieResponse>

    @GET("/")
    suspend fun getParticularDetails(
        @Query("apikey") apikey: String = "c23f0fa4",
        @Query("i") imdbId :String
    ) : Response<MovieDetailResponse>
}