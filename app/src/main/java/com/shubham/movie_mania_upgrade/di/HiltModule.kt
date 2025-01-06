package com.shubham.movie_mania_upgrade.di

import com.shubham.lib_permission.permissionManager.PermissionManager
import com.shubham.movie_mania_upgrade.remote.IMovieImplementor
import com.shubham.movie_mania_upgrade.remote.MovieRepository
import com.shubham.movie_mania_upgrade.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@InstallIn(SingletonComponent::class) // define the singleton Component used all over the app. This defines
// the Hilt to make the dependency using and inject where required.

// make this a module component
@Module
object HiltModule {


    /***
     * This will provide the retrofit instance at runtime.
     * using Hilt we will get the dependencies which result in the
     * singleton pattern follow with solution to not to create the dependencies manually.
     ****/

    private fun returnHttpLoggingInterceptor(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Logs request and response bodies
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    fun provideMovieRetrofitInstance(): IMovieImplementor {
        return Retrofit.Builder()
            .baseUrl(Constants.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(returnHttpLoggingInterceptor())
            .build()
            .create(IMovieImplementor::class.java)
    }

    @Provides
    fun provideRepositoryInstance(): MovieRepository {
        return MovieRepository(provideMovieRetrofitInstance())
    }

    @Provides
    fun permissionManager(): PermissionManager{
        return PermissionManager()
    }


}