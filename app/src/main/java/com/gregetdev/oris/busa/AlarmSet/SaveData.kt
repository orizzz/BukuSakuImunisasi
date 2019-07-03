package com.gregetdev.oris.busa.AlarmSet

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.util.Log
import java.util.*


class SaveData {

    private val APP_SHARED_PREFS = "RemindMePref"

    private var appSharedPrefs: SharedPreferences
    private var prefsEditor: SharedPreferences.Editor

    private val reminderStatus = "reminderStatus"
    private val hour = "hour"
    private val min = "min"


    @SuppressLint("CommitPrefEdits")
    constructor(context: Context){
        this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS,Context.MODE_PRIVATE)
        this.prefsEditor = appSharedPrefs.edit()
        Log.d("Alarm","Data saved context: $context")
    }

    fun SaveData(hours: Int, minutes: Int) {

        prefsEditor.putInt("hour",hours)
        prefsEditor.putInt("minute",minutes)
        prefsEditor.commit()
        Log.d("Alarm","Data saved : $hours:$minutes")
    }

    fun getHour():Int{
        return appSharedPrefs.getInt("hour",0)
    }


    fun getMinutes():Int{
        return appSharedPrefs.getInt("minute",0)
    }

    /*fun setAlarm(context: Context){

        val hours: Int = getHour()
        val minutes: Int = getMinutes()
        val cls = BroadcastReceiver::class.java

        val calendar = Calendar.getInstance()

        val setcalendar = Calendar.getInstance()
        setcalendar.set(Calendar.HOUR_OF_DAY, hours)
        setcalendar.set(Calendar.MINUTE, minutes)
        setcalendar.set(Calendar.SECOND, 0)

        if (setcalendar.before(calendar))
            setcalendar.add(Calendar.DATE, 1)

        val receiver = ComponentName(context, cls)
        val pm = context.packageManager

        pm.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

        val intent1 = Intent(context, cls)
        val pendingIntent =
            PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT)
        val am = context.getSystemService(ALARM_SERVICE) as AlarmManager
        am.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            setcalendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )


        *//*val intent = Intent(context, BroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context,0,intent,0)
        intent.putExtra("Massage","Alarm menyala")
        intent.action="com.busa.alarm"


        val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY, pendingIntent)*//*

    }*/

}