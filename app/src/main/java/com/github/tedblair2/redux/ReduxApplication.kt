package com.github.tedblair2.redux

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ReduxApplication:Application() {
    override fun onCreate() {
        super.onCreate()
    }
}