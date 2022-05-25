package com.example.stopwatch



import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class ApplicationClass:Application() {
    companion object {
        const val CHANNEL_ID="channel1"
    }

    override fun onCreate() {
        super.onCreate()
        // need android 8(Oreo) for notification
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val notificationChannel= NotificationChannel(CHANNEL_ID,"StopWatch",NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.description="a stopwatch for you"
            val notificationManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)

        }
    }
}