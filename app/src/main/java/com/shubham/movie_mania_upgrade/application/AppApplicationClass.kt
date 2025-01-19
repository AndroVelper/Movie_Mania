package com.shubham.movie_mania_upgrade.application

import android.app.Application
import com.shubham.movie_mania_upgrade.di.HiltModule
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class AppApplicationClass :Application() {

    override fun onCreate() {
        super.onCreate()

    }
}

