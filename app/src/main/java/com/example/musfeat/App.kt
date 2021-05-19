package com.example.musfeat

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application()

object AppConstants {
    const val USER_NAME = "USER_NAME"
    const val USER_ID = "USER_ID"
    const val CHANNEL_ID = "CHANNEL_ID"
}
