package com.gregetdev.oris.busa.AlarmSet

import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.gregetdev.oris.busa.HomeMenu
import com.gregetdev.oris.busa.Profile
import com.gregetdev.oris.busa.Profile_Bayi.Alarm
import com.gregetdev.oris.busa.R
import java.util.*
import kotlin.reflect.KClass

class AlarmNotification {
    val DAILY_REMINDER_REQUEST_CODE = 100
    val TAG = "NotificationScheduler"

    fun setReminder(
        context: Context,
        cls: Class<*>,
        hour:Int,
        minute:Int
    ){
        val calendar = Calendar.getInstance()

        val setcalendar = Calendar.getInstance()
        setcalendar.set(Calendar.HOUR_OF_DAY, hour)
        setcalendar.set(Calendar.MINUTE, minute)
        setcalendar.set(Calendar.SECOND, 0)
        cancelReminder(context, cls)

        if(setcalendar.before(calendar))
            setcalendar.add(Calendar.DATE,1)

        val receiver = ComponentName(context,cls)
        val packageManager = context.packageManager

        packageManager.setComponentEnabledSetting(receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP)

        val intent = Intent(context,cls)
        val pendingIntent = PendingIntent.getBroadcast(context,DAILY_REMINDER_REQUEST_CODE,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,setcalendar.timeInMillis,pendingIntent)
    }

    fun cancelReminder(context: Context, cls: Class<*>) {
        val receiver = ComponentName(context,cls)
        val packageManager = context.packageManager

        packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )

        val intent = Intent(context,cls)
        val pendingIntent = PendingIntent.getBroadcast(context,DAILY_REMINDER_REQUEST_CODE,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()

    }

    fun showNotification(context: Context,cls:Class<*>,title:String,content:String){
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationIntent = Intent(context, cls)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(cls)
        stackBuilder.addNextIntent(notificationIntent)

        val pendingIntent =
            stackBuilder.getPendingIntent(DAILY_REMINDER_REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context)

        val notification = builder.setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(true)
            .setSound(alarmSound)
            .setSmallIcon(R.drawable.needle_200x200)
            .setContentIntent(pendingIntent).build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(DAILY_REMINDER_REQUEST_CODE, notification)

    }
}