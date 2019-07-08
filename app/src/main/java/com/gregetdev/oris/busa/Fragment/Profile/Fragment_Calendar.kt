@file:Suppress("DEPRECATION")

package com.gregetdev.oris.busa.Fragment.Profile


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.gregetdev.oris.busa.R
import com.gregetdev.oris.busa.model.DataImunisasiModel
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

private const val BayiKEY = "param1"

/**
 * A simple [Fragment] subclass.
 *
 */
class Fragment_Remider : Fragment() {


    private lateinit var database: FirebaseDatabase

    private lateinit var CalendarView: CalendarView
    private lateinit var jadwalImunisasi_string: String
    private lateinit var jadwalImunisasi_date: Date

    private var BayiKey_profile: String? = null
    private var event: MutableList<EventDay> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            BayiKey_profile = it.getString(BayiKEY)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val key = BayiKey_profile.toString()
        database = FirebaseDatabase.getInstance()
        val data_imunisasi = database.getReference("/Data Imunisasi/$key")

        data_imunisasi.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

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

            }

        })



        Reminder_calendarView.showCurrentMonthPage()


    }

    @SuppressLint("SimpleDateFormat")
    private fun getReadableDate(calendar: Calendar): String? {
        val dformat = SimpleDateFormat("dd/MM/YYYY")
        return dformat.format(calendar.time)
    }

    companion object {

        @JvmStatic
        fun newInstance(Key: String) =
            Fragment_Remider().apply {
                arguments = Bundle().apply {
                    putString(BayiKEY, Key)


                }

            }
    }

}