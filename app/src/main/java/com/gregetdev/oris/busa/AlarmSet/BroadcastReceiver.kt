package com.gregetdev.oris.busa.AlarmSet

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import com.gregetdev.oris.busa.HomeMenu

class BroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("Alarm","Broadcast Intent: $intent")
        Log.d("Alarm","Broadcast context: $context")
/*        if (intent!!.action.equals("com.busa.alarm",true)){
            val bundle = intent.extras
            val notif = AlarmNotification()
            notif.notify(context!!,bundle.getString("Massage"),10)

        }else if(intent!!.action.equals("android.intent.action.BOOT_COMPLETED",true)){
            val saveData = SaveData(context!!)
            saveData.setAlarm()

        }*/

        if(intent!!.action.equals(Intent.ACTION_BOOT_COMPLETED,true)){
            Log.d("Alarm", "onReceive: BOOT_COMPLETED")
            val Savedata = SaveData(context!!)
            AlarmNotification().setReminder(
                context,
                BroadcastReceiver::class.java,
                Savedata.getHour(),
                Savedata.getMinutes())
            return
        }

        AlarmNotification().showNotification(
            context!!,HomeMenu::class.java,
            "Alarm Imunisasi","Jangan lupa melakukan Imunisasi")
    }
}