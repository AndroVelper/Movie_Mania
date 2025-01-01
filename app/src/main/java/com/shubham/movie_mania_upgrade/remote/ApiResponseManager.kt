package com.shubham.movie_mania_upgrade.remote

sealed class CommonResponseManager<T> {
    data class Loading<T>(val isLoading : Boolean) : CommonResponseManager<T>()
    data class Error<T>(val errorMessage : String , val errorCode : Int) : CommonResponseManager<T>()
    data class Success<T>(val data : T) : CommonResponseManager<T>()
}