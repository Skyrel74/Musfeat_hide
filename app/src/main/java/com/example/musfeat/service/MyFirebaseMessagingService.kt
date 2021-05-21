package com.example.musfeat.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.musfeat.AppConstants
import com.example.musfeat.R
import com.example.musfeat.util.FirestoreUtil
import com.example.musfeat.view.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val NOTIFICATION_CHANNEL_ID = "com.example.musfeat.service"
    private val NOTIFICATION_ID = 100

    companion object {
        fun addTokenToFirestore(newRegistrationToken: String?) {
            if (newRegistrationToken == null) throw NullPointerException("FCM token is null.")

            FirestoreUtil.getFCMRegistrationTokens(FirebaseAuth.getInstance().currentUser!!.uid) {}
        }

        fun sendNotification(context: Context, json: JSONObject) {
            val request: JsonObjectRequest = object : JsonObjectRequest(
                Method.POST,
                AppConstants.NOTIFICATION_URL,
                json,
                Response.Listener { response ->

                    Log.d("TAG", "onResponse: $response")
                },
                Response.ErrorListener {

                    Log.d("TAG", "onError: $it")
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val map: MutableMap<String, String> = HashMap()

                    map["Authorization"] = "key=" + AppConstants.SERVER_KEY
                    map["Content-type"] = "application/json"
                    return map
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }

            val requestQueue = Volley.newRequestQueue(context)
            request.retryPolicy = DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )

            requestQueue.add(request)
        }
    }

    private fun createNormalNotification(
        title: String,
        message: String,
        uId: String,
        uName: String,
        channelId: String,
        notificationFLag: String
    ) {

        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(this, channelId)
        builder.setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.ic_music_note_24)
            .setAutoCancel(true)
            .setColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, null))
            .setSound(uri)

        val intent = Intent(this, MainActivity::class.java)

        intent.putExtra(AppConstants.USER_ID, uId)
        intent.putExtra(AppConstants.USER_NAME, uName)
        intent.putExtra(AppConstants.CHANNEL_ID, channelId)
        intent.putExtra(AppConstants.NOTIFICATION_FLAG_CHAT, notificationFLag)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        builder.setContentIntent(pendingIntent)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(Random().nextInt(85 - 65), builder.build())

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createOreoNotification(
        title: String,
        message: String,
        uId: String,
        uName: String,
        channelId: String,
        notificationFLag: String
    ) {

        val channel = NotificationChannel(
            AppConstants.CHANNEL_ID,
            "Message",
            NotificationManager.IMPORTANCE_HIGH
        )

        channel.setShowBadge(true)
        channel.enableLights(true)
        channel.enableVibration(true)
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        val intent = Intent(this, MainActivity::class.java)

        intent.putExtra(AppConstants.USER_ID, uId)
        intent.putExtra(AppConstants.USER_NAME, uName)
        intent.putExtra(AppConstants.CHANNEL_ID, channelId)
        intent.putExtra(AppConstants.NOTIFICATION_FLAG_CHAT, notificationFLag)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val notification = Notification.Builder(this, AppConstants.CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, null))
            .setContentIntent(pendingIntent)
            .build()

        manager.notify(100, notification)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("FMC", remoteMessage.data.toString())
        if (remoteMessage.data.isNotEmpty()) {

            val title = remoteMessage.data["title"]
            val body = remoteMessage.data["body"]
            val message = remoteMessage.data["message"]
            val uName = remoteMessage.data["uName"]
            val uId = remoteMessage.data["uId"]
            val channelId = remoteMessage.data["channelId"]
            val notificationFlag = remoteMessage.data["notificationFlag"]

            showNotification(applicationContext, title, body)
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
                createOreoNotification(
                    title!!,
                    message!!,
                    uId!!,
                    uName!!,
                    channelId!!,
                    notificationFlag!!
                )
            else
                createNormalNotification(
                    title!!,
                    message!!,
                    uId!!,
                    uName!!,
                    channelId!!,
                    notificationFlag!!
                )
        } else {
            val title = remoteMessage.notification!!.title
            val body = remoteMessage.notification!!.body
            showNotification(applicationContext, title, body)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        if (FirebaseAuth.getInstance().currentUser != null)
            addTokenToFirestore(token)
    }

    private fun showNotification(
        context: Context,
        title: String?,
        message: String?
    ) {
        val ii = Intent(context, MainActivity::class.java)
        ii.data = Uri.parse("custom://" + System.currentTimeMillis())
        ii.action = "actionstring" + System.currentTimeMillis()
        ii.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pi =
            PendingIntent.getActivity(context, 0, ii, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification: Notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Log.e("Notification", "Created in up to orio OS device");
            notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setOngoing(true)
                .setSmallIcon(getNotificationIcon())
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pi)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setWhen(System.currentTimeMillis())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title).build()
            val notificationManager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                title,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
            notificationManager.notify(NOTIFICATION_ID, notification)
        } else {
            notification = NotificationCompat.Builder(context)
                .setSmallIcon(getNotificationIcon())
                .setAutoCancel(true)
                .setContentText(message)
                .setContentIntent(pi)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title).build()
            val notificationManager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }

    private fun getNotificationIcon() = R.mipmap.ic_launcher
}
