package com.gregetdev.oris.busa.Profile_Bayi

import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.gregetdev.oris.busa.AlarmSet.AlarmNotification
import com.gregetdev.oris.busa.AlarmSet.BroadcastReceiver
import com.gregetdev.oris.busa.AlarmSet.SaveData
import com.gregetdev.oris.busa.R
import kotlinx.android.synthetic.main.activity_alarm.*

class Alarm : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    val AlarmTime = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        val key = intent.getStringExtra("BayiKey").toString()
        database = FirebaseDatabase.getInstance()
        val dataImunisasi = database.getReference("/Data Imunisasi/$key")
        val load = SaveData(applicationContext)
        Alarm_jam_text_view.text = readableTime(load.getHour(),load.getMinutes())

        Alarm_jam_text_view.setOnClickListener{

            val SetTimePicker =
                TimePickerDialog.OnTimeSetListener{ view, hour, minute->

                    AlarmTime.run {
                        set(Calendar.HOUR_OF_DAY,hour)
                        set(Calendar.MINUTE,minute)
                    }

                    Alarm_jam_text_view.text = readableTime(hour,minute)
                }

            TimePickerDialog(
                this,
                SetTimePicker,
                AlarmTime.get(Calendar.HOUR_OF_DAY),
                AlarmTime.get(Calendar.MINUTE),
                true
            ).show()

        }

        Alarm_button.setOnClickListener {
            /*CreateNotification()*/
            Log.d("Alarm","${AlarmTime.get(Calendar.HOUR_OF_DAY)}:${AlarmTime.get(Calendar.MINUTE)}")

            val saveData = SaveData(applicationContext)

            saveData.SaveData(
                AlarmTime.get(Calendar.HOUR_OF_DAY),
                AlarmTime.get(Calendar.MINUTE)
            )
            AlarmNotification().setReminder(
                this@Alarm,
                BroadcastReceiver::class.java,
                saveData.getHour(),
                saveData.getMinutes()
            )
            Alarm_jam_text_view.alpha = 0.4f
        }


    }

    private fun readableTime(hour:Int,minute:Int): String {
        val Shour : String
        val Sminute : String

        if (hour in 0..9){
            Shour = "0$hour"
        } else {
            Shour = "$hour"
        }
        if (minute in 0..9){
            Sminute = "0$minute"
        } else {
            Sminute = "$minute"
        }

        return "$Shour:$Sminute"
    }
}
