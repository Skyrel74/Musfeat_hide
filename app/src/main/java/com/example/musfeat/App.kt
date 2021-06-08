package com.example.musfeat

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application()

object AppConstants {
    const val USER_NAME = "USER_NAME"
    const val USER_ID = "USER_ID"
    const val CHANNEL_ID = "CHANNEL_ID"
    const val NOTIFICATION_FLAG_CHAT = "NOTIFICATION_FLAG_CHAT"
    const val NOTIFICATION_URL = "https://fcm.googleapis.com/fcm/send"
    const val SERVER_KEY =
        "AAAAhDoYKNY:APA91bF1SLnUzf67Sd898oidSvsRrUe6XtRbXBFH6w7o2prhVDEQTgK3aGQuQZ6fyTZ8DKjJJ2GgKzUiJx-YgyRF8ubyttZX5tYXYyuUvIiP7LCVLFfUZu52RZVNn3hXShx_MlujOuTr"
    const val MAP_KIT_API_KEY = "420d0cd4-cc68-4071-a5b3-9571a74c1c5c"
    const val RC_LOCATION = 1488
    const val CALENDAR_CACHE_TIME = 1_800_000L
}
