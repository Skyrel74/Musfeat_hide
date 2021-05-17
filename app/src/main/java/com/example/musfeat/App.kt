package com.example.musfeat

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application()

object AppConstants {
    val USER_NAME = "USER_NAME"
    val USER_ID = "USER_ID"
}
