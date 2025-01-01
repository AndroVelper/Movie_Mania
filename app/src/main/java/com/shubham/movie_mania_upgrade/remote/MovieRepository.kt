package com.shubham.movie_mania_upgrade.remote

class MovieRepository( private val movieImplementor: IMovieImplementor) {



    /***
     * This will return the user searched Movie searched in the search bar
     * @param userSearchedMovie : This is the string representing the name of the movie Searched By the user.
     * @param requiredPageNumber : this is the next page number which data is required in the App.
     * **/

    suspend fun getMovieData(
        userSearchedMovie: String,
        requiredPageNumber: Int
    ) = movieImplementor.getMovieData(
        searchMovie = userSearchedMovie,
        pageNumber = requiredPageNumber
    )


    suspend fun getMovieDetails(
        imdbId: String,
    ) = movieImplementor.getParticularDetails(
        imdbId = imdbId
    )


}