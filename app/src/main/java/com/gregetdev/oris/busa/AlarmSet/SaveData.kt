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


}