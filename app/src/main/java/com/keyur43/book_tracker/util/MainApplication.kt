package com.keyur43.book_tracker.util

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.Forest.plant(Timber.DebugTree())
    }
}