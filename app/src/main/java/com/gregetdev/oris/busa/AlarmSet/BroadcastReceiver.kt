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
        if(intent!!.action.equals(Intent.ACTION_BOOT_COMPLETED,true)){
            Log.d("Alarm", "onReceive: BOOT_COMPLETED")
            val Savedata = SaveData(context!!)
            AlarmNotification().setReminder(context, BroadcastReceiver::class.java, Savedata.getHour(), Savedata.getMinutes())
            return}
        AlarmNotification().showNotification(
            context!!,HomeMenu::class.java,
            "Alarm Imunisasi","Jangan lupa melakukan Imunisasi")
    }
}