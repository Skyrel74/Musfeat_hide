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
}
