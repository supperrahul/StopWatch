package com.example.stopwatch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.stopwatch.MainActivity.Companion.binding
import kotlin.system.exitProcess

class NotificationReceiver : BroadcastReceiver() {
    var globalContext: Context? = null
    object ACTIONS{
        const val START = "0";
        const val PAUSE = "1";
        const val PIN = "2";
        const val RESET = "3";
        const val OPEN_APP="4"

    }
    override fun onReceive(context: Context?, intent: Intent?) {
        globalContext=context
        when (intent?.action) {
            ACTIONS.OPEN_APP->{
                context?.startActivity( Intent(context,MainActivity().javaClass))
            } 
            ACTIONS.PAUSE->{
                Toast.makeText(context, "paused", Toast.LENGTH_SHORT).show()
                //binding from MainActivity
                binding.playpausebtn.performClick()
            }
            ACTIONS.START->{
                Toast.makeText(context, "started", Toast.LENGTH_SHORT).show()
                binding.playpausebtn.performClick()

            }

            ACTIONS.PIN->{
                binding.lapbtn.performClick()
            }

            ACTIONS.RESET->{
                binding.stop.performClick()
            }

        }

    }
}

