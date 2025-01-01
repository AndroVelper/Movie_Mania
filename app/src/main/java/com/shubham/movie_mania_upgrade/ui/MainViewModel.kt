package com.shubham.movie_mania_upgrade.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.shubham.movie_mania_upgrade.data.MovieDetailResponse
import com.shubham.movie_mania_upgrade.remote.CommonResponseManager
import com.shubham.movie_mania_upgrade.remote.MovieRepository
import com.shubham.movie_mania_upgrade.ui.movie.MoviePaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val movieApiRepo: MovieRepository) : ViewModel() {

    private val _movieDetails: MutableLiveData<CommonResponseManager<MovieDetailResponse?>> =
        MutableLiveData()
    val movieDetails: LiveData<CommonResponseManager<MovieDetailResponse?>> get() = _movieDetails

    private val query: MutableLiveData<String> = MutableLiveData("fast")
    val list = query.switchMap { query ->
        Pager(PagingConfig(pageSize = 8)) {
            MoviePaging(query, movieApiRepo)
        }.liveData.cachedIn(viewModelScope)
    }

    fun setQuery(search: String) {
        query.postValue(search)
    }


    fun getTheMovieDetails(imdbId: String) {
        _movieDetails.postValue(CommonResponseManager.Loading(true))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val movieDetails = movieApiRepo.getMovieDetails(imdbId)
                if (movieDetails.isSuccessful) {
                    val data = movieDetails.body()
                    _movieDetails.postValue(CommonResponseManager.Success(data))
                } else {
                    _movieDetails.postValue(
                        CommonResponseManager.Error(
                            movieDetails.errorBody().toString(), movieDetails.code()
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
}