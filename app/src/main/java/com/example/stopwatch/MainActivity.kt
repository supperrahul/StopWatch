package com.example.stopwatch

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.NotificationManager
import android.content.ComponentName
import android.content.DialogInterface
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.example.stopwatch.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.System.exit
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), ServiceConnection {
    var appService : AppService? = null
    lateinit var runnable:Runnable
    var milisecond=0
    var second=0
    var minitues=0
    var hour=0
    var strmilisecond=""
    var strsecond=""
    var strminitues=""
    var strhour=""
    var index=1
    var holder = mutableListOf<View>()
    companion object{
        var isPlaying = false
        lateinit var binding:ActivityMainBinding
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        window.navigationBarColor=getColor(R.color.voilet_low)
        supportActionBar?.hide()
       playpausebtn.setOnClickListener {
            startOrStopWatch()
        }
        val intent =Intent(baseContext,AppService().javaClass)
        bindService(intent, this, BIND_AUTO_CREATE)
        startService(intent)
      lapbtn.setOnClickListener {
            lapbox.removeAllViews()
            val inflater = LayoutInflater.from(this)
            var view = inflater.inflate(R.layout.ui,null)
            val params = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            params.bottomMargin = 10
            params.topMargin = 10
            view.layoutParams=params
            view.findViewById<TextView>(R.id.num).text=index.toString()
            var string = ""
            if(hur.visibility==View.VISIBLE){
                string+=hur.text
                string+=":"
            }

            string+="${min.text}:${sec.text}.${msec.text}"
            view.findViewById<TextView>(R.id.time).text=string
            holder.add(view)
            try {
                var i = holder.size
                Toast.makeText(this, i.toString(), Toast.LENGTH_SHORT).show()
                while (i>0){

                    lapbox.addView(holder[i-1])

                    i--
                }

            }catch (e:Exception){
                Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
            }

            ++index
        }

        runnable = Runnable {


            if(isPlaying){
                if(milisecond>99){
                    milisecond=0
                    ++second
                }
                if(second>59){
                    milisecond=0
                    second=0
                    ++minitues
                }
                if(minitues>59){
                    milisecond=0
                    second=0
                    minitues=0
                    ++hour
                }
                if(minitues>=58){
                    hur.visibility=View.VISIBLE
                    d1.visibility=View.VISIBLE
                }
                if(second>=58){
                    min.visibility=View.VISIBLE
                    d2.visibility=View.VISIBLE
                }
                if(milisecond<10){
                    strmilisecond="0"+milisecond
                }else{
                    strmilisecond=milisecond.toString()
                }
                if(second<10){
                    strsecond= "0$second"
                }else{
                    strsecond=second.toString()
                }
                if(minitues<10){
                    strminitues= "0$minitues"
                }else{
                    strminitues=minitues.toString()
                }
                if(hour<10){
                    strhour= "0$hour"
                }else{
                    strhour=hour.toString()
                }

                msec.text=strmilisecond
                sec.text=strsecond
                min.text=strminitues
                hur.text=strhour
                appService?.showNotification("Running...","${min.text}:${sec.text}")
                milisecond+=10

            }




            Handler(Looper.getMainLooper()).postDelayed(runnable,100)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable,100)

        stop.setImageDrawable(getDrawable(R.drawable.ic_refresh))

        stop.setOnClickListener{
            appService?.cancelNotification()
            lapbox.removeAllViews()
            isPlaying = false
            holder= mutableListOf()

            index=1

             milisecond=0
             second=0
             minitues=0
             hour=0

             strmilisecond=""
             strsecond=""
             strminitues=""
             strhour=""

            msec.text="00"
            sec.text="00"
            min.text="00"
            hur.text="00"

            playpausebtn.setImageDrawable(getDrawable(R.drawable.ic_play))
            it.visibility= View.GONE
            hur.visibility=View.GONE
            d1.visibility=View.GONE
            min.visibility=View.GONE
            d2.visibility=View.GONE
            lapbtn.visibility=View.GONE
        }

        lapbtn.setImageDrawable(getDrawable(R.drawable.ic_pin))
    }

    private fun startOrStopWatch() {
        isPlaying =
       if(isPlaying){
           Handler(Looper.myLooper()!!).postDelayed({appService?.showNotification("Paused","${min.text}:${sec.text}") },120)
           lapbtn.visibility=View.GONE
            playpausebtn.setImageDrawable(getDrawable(R.drawable.ic_play))
            false // isPlaying = false
        }else{
           appService?.showNotification("Running...","${min.text}:${sec.text}")
           stop.visibility=View.VISIBLE
            lapbtn.visibility=View.VISIBLE
            playpausebtn.setImageDrawable(getDrawable(R.drawable.ic_pause))
            true // isPlaying = true
        }


    }

    override fun onBackPressed() {
        if (isPlaying) {
            var dialog = AlertDialog.Builder(this)
            dialog.setTitle("Close App?")
            dialog.setCancelable(true)
            dialog.setMessage("confirm to close the app? stopwatch is running.")
            dialog.setPositiveButton("Hmm",object :DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    val notificationManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.cancelAll()
                    finishAffinity()
                    exit(1)
                }
            })
            dialog.setNegativeButton("No",object :DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {

                }
            })

            dialog.show()

        } else {
            finishAffinity()
        }
    }
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as AppService.MyBinder
         appService = binder.currentService()

    }

    override fun onServiceDisconnected(name: ComponentName?) {
        appService = null
    }


}