package com.gregetdev.oris.busa.Profile_Bayi

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.applandeo.materialcalendarview.EventDay
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.gregetdev.oris.busa.R
import com.gregetdev.oris.busa.model.DataImunisasiModel
import kotlinx.android.synthetic.main.activity_calendar.*
import java.text.SimpleDateFormat
import java.util.Calendar

class Calendar : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private var event: MutableList<EventDay> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        val key = intent.getStringExtra("BayiKey").toString()
        database = FirebaseDatabase.getInstance()
        val data_imunisasi = database.getReference("/Data Imunisasi/$key")

        data_imunisasi.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataSnapshot.children.forEach {
                    val data = it.getValue(DataImunisasiModel::class.java)
                    val calendar = Calendar.getInstance()
                    if (data != null) {
                        if (data.Tgl_imunisasi != null) {
                            val tanggal = data.Tgl_imunisasi.toString()
                            val delimiter = "/"
                            val part = tanggal.split(delimiter)

                            val year = part[2].toInt()
                            val month = part[1].toInt()-1
                            val dayOfMonth = part[0].toInt()

                            calendar.run {
                                set(Calendar.YEAR, year)
                                set(Calendar.MONTH, month)
                                set(Calendar.DAY_OF_MONTH, dayOfMonth)
                            }

                            event.add(EventDay(calendar,R.mipmap.ic_launcher_srynge))

                        }
                    }

                }
                Reminder_calendarView.setEvents(event)
                Reminder_calendarView.setOnDayClickListener{
                    val Clicked = it.calendar
                    val ReadableDate = getReadableDate(it.calendar).toString()
                    val data = dataSnapshot.value



                    Log.d("Calendar","$data")

                }
            }

        })



        Reminder_calendarView.showCurrentMonthPage()

    }

    @SuppressLint("SimpleDateFormat")
    private fun getReadableDate(calendar: Calendar): String? {
        val dformat = SimpleDateFormat("dd/MM/YYYY")
        return dformat.format(calendar.time)
    }
}
