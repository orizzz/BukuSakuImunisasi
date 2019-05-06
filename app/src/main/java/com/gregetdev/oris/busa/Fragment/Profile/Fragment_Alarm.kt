package com.gregetdev.oris.busa.Fragment.Profile


import android.app.Dialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TimePicker
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.gregetdev.oris.busa.R
import com.gregetdev.oris.busa.model.DataImunisasiModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_alarm.*
import kotlinx.android.synthetic.main.view_alarm_list.*
import kotlinx.android.synthetic.main.view_alarm_list.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val BayiKEY = "param1"


class Fragment_Alarm : Fragment() {

    private lateinit var database: FirebaseDatabase

    private var bayiKey: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bayiKey = it.getString(BayiKEY)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_alarm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val key = bayiKey.toString()
        database = FirebaseDatabase.getInstance()
        val dataImunisasi = database.getReference("/Data Imunisasi/$key")

        Alarm_jam_text_view.setOnClickListener{
            val AlarmTime = Calendar.getInstance()


            val SetTimePicker =
                TimePickerDialog.OnTimeSetListener{view, hour,minute->

                    AlarmTime.run {
                        set(Calendar.HOUR_OF_DAY,hour)
                        set(Calendar.MINUTE,minute)
                    }

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


                    Alarm_jam_text_view.text = "$Shour:$Sminute"
                }

            TimePickerDialog(
                activity,
                SetTimePicker,
                AlarmTime.get(Calendar.HOUR_OF_DAY),
                AlarmTime.get(Calendar.MINUTE),
                true
            ).show()





        }

       /* TampilListAlarm(bayiKey)*/

    }

    /*private fun TampilListAlarm(bayiKey: String?) {
        val data_imunisasi = FirebaseDatabase.getInstance().getReference("/Data Imunisasi/$bayiKey")
            .orderByChild("umurPemberian")

        val options = FirebaseRecyclerOptions.Builder<DataImunisasiModel>()
            .setQuery(data_imunisasi, DataImunisasiModel::class.java)
            .build()

        val adapterFirebase = object : FirebaseRecyclerAdapter<DataImunisasiModel, ListDataAlarmHolder>(options){

            override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ListDataAlarmHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.view_alarm_list,parent,false)
                return ListDataAlarmHolder(view)
            }

            override fun onBindViewHolder(holder: ListDataAlarmHolder, position: Int, model: DataImunisasiModel) {
                holder.mView.Alarm_keterangan.text = "Imunisasi \n${getRef(position).key.toString()}"

                holder.mView.Alarm_jam_text_view.setOnClickListener{
                    val AlarmTime = Calendar.getInstance()


                    val SetTimePicker =
                        TimePickerDialog.OnTimeSetListener{view, hour,minute->

                            AlarmTime.run {
                                set(Calendar.HOUR_OF_DAY,hour)
                                set(Calendar.MINUTE,minute)
                            }

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


                            holder.mView.Alarm_jam_text_view.text = "$Shour:$Sminute"
                    }

                    TimePickerDialog(
                        activity,
                        SetTimePicker,
                        AlarmTime.get(Calendar.HOUR_OF_DAY),
                        AlarmTime.get(Calendar.MINUTE),
                        true
                        ).show()





                }

                holder.mView.Alarm_switch.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
                    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                        if (isChecked){
                            return
                        } else {
                            if (!isChecked) {
                                alarm_layout.setBackgroundColor(resources.getColor(R.color.Grey))
                            }
                        }
                    }

                })



            }


        }

        adapterFirebase.startListening()
        List_alarm_recylerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        List_alarm_recylerview.adapter = adapterFirebase

    }*/

    companion object {
        @JvmStatic
        fun newInstance(key: String) =
            Fragment_Alarm().apply {
                arguments = Bundle().apply {
                    putString(BayiKEY, key)

                }
            }
    }
}

class ListDataAlarmHolder(var mView: View) : RecyclerView.ViewHolder(mView)


