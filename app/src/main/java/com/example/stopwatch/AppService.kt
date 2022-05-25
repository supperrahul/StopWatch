package com.example.stopwatch



import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.view.View

import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.stopwatch.MainActivity.Companion.isPlaying
import java.lang.System.exit


class AppService : Service(){

    var myBinder=MyBinder()

    override fun onBind(intent: Intent): IBinder? {

        return myBinder
    }

    override fun onCreate() {
        super.onCreate()


    }



    inner class MyBinder:Binder(){
        
        fun currentService():AppService{
            return  this@AppService
        }
    }

    fun showNotification(title:String,text:String){


        val appIntent = Intent(baseContext,NotificationReceiver().javaClass).setAction(ACTIONS.OPEN_APP)
        val appPendingIntent = PendingIntent.getBroadcast(baseContext,0,appIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        var ntitle1 :String
        var nicon1: Int;
        var ntitle2:String
        var nicon2:Int
        lateinit var  actionPendingIntent1:PendingIntent
        lateinit var  actionPendingIntent2:PendingIntent
        if(isPlaying){
            ntitle1="pause"
            nicon1=R.drawable.ic_pause
            ntitle2="pin"
            nicon2=R.drawable.ic_pin

            val actionIntent1 = Intent(baseContext,NotificationReceiver().javaClass).setAction(ACTIONS.PAUSE)
             actionPendingIntent1 = PendingIntent.getBroadcast(baseContext,0,actionIntent1,PendingIntent.FLAG_UPDATE_CURRENT)
            val actionIntent2 = Intent(baseContext,NotificationReceiver().javaClass).setAction(ACTIONS.PIN)
            actionPendingIntent2 = PendingIntent.getBroadcast(baseContext,0,actionIntent2,PendingIntent.FLAG_UPDATE_CURRENT)

        }else{
            ntitle1="start"
            nicon1=R.drawable.ic_play
            ntitle2="reset"
            nicon2=R.drawable.ic_refresh
            val actionIntent1 = Intent(baseContext,NotificationReceiver().javaClass).setAction(ACTIONS.START)
             actionPendingIntent1 = PendingIntent.getBroadcast(baseContext,0,actionIntent1,PendingIntent.FLAG_UPDATE_CURRENT)

            val actionIntent2 = Intent(baseContext,NotificationReceiver().javaClass).setAction(ACTIONS.RESET)
            actionPendingIntent2 = PendingIntent.getBroadcast(baseContext,0,actionIntent2,PendingIntent.FLAG_UPDATE_CURRENT)
        }

        try {

            NotificationCompat.Builder(this, ApplicationClass.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_pin)
                .setColor(applicationContext.getColor(R.color.purple_700))
                .setContentIntent(appPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOnlyAlertOnce(true)
                .setSound(null)
                .addAction(nicon1,ntitle1,actionPendingIntent1)
                .addAction(nicon2,ntitle2,actionPendingIntent2)
                .build().apply {
                    startForeground(1,this)
               }


        }catch (e:Exception){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    fun cancelNotification(){

        val notificationManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()

    }

    object ACTIONS{
        val START = "0";
        val PAUSE = "1";
        val PIN = "2";
        val RESET = "3";
        val OPEN_APP="4"

    }
}