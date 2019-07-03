package com.gregetdev.oris.busa.Fragment.Profile


import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.gregetdev.oris.busa.AlarmSet.AlarmNotification
import com.gregetdev.oris.busa.AlarmSet.BroadcastReceiver
import com.gregetdev.oris.busa.AlarmSet.SaveData
import com.gregetdev.oris.busa.R
import com.gregetdev.oris.busa.model.AlarmModel
import kotlinx.android.synthetic.main.fragment_alarm.*
import kotlinx.android.synthetic.main.view_alarm_list.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val BayiKEY = "param1"


class Fragment_Alarm : Fragment() {

    private lateinit var database: FirebaseDatabase


    private var bayiKey: String? = null
    val AlarmTime = Calendar.getInstance()

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
        val load = SaveData(context!!)
        Alarm_jam_text_view.text = readableTime(load.getHour(),load.getMinutes())

        /*alarm_layout.isEnabled = Alarm_switch.isChecked*/
        Alarm_jam_text_view.setOnClickListener{

            val SetTimePicker =
                TimePickerDialog.OnTimeSetListener{view, hour,minute->

                    AlarmTime.run {
                        set(Calendar.HOUR_OF_DAY,hour)
                        set(Calendar.MINUTE,minute)
                    }

                    Alarm_jam_text_view.text = readableTime(hour,minute)
                }

            TimePickerDialog(
                activity,
                SetTimePicker,
                AlarmTime.get(Calendar.HOUR_OF_DAY),
                AlarmTime.get(Calendar.MINUTE),
                true
            ).show()

        }

        Alarm_button.setOnClickListener {
            /*CreateNotification()*/
            Log.d("Alarm","${AlarmTime.get(Calendar.HOUR_OF_DAY)}:${AlarmTime.get(Calendar.MINUTE)}")

            val saveData = SaveData(context!!)

            saveData.SaveData(
                AlarmTime.get(Calendar.HOUR_OF_DAY),
                AlarmTime.get(Calendar.MINUTE)
            )
            AlarmNotification().setReminder(
                activity!!,
                BroadcastReceiver::class.java,
                saveData.getHour(),
                saveData.getMinutes()
            )
            Alarm_jam_text_view.alpha = 0.4f
        }



        TampilListAlarm(key)

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



    private fun TampilListAlarm(bayiKey: String?) {
        val Alarm = FirebaseDatabase.getInstance().getReference("/Alarm/$bayiKey")
            .orderByChild("urutan")

        val options = FirebaseRecyclerOptions.Builder<AlarmModel>()
            .setQuery(Alarm, AlarmModel::class.java)
            .build()

        val adapterFirebase = object : FirebaseRecyclerAdapter<AlarmModel, ListDataAlarmHolder>(options){

            override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ListDataAlarmHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.view_alarm_list,parent,false)
                return ListDataAlarmHolder(view)
            }

            override fun onBindViewHolder(holder: ListDataAlarmHolder, position: Int, model: AlarmModel) {
                holder.mView.Alarm_keterangan.text = "Imunisasi \n${getRef(position).key.toString()}"
                holder.mView.Alarm_tgl.text = model.Tgl_alarm

            }

        }

        adapterFirebase.startListening()
        List_alarm_recylerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        List_alarm_recylerview.adapter = adapterFirebase

    }

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


