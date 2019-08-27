package com.gregetdev.oris.busa.Fragment.Profile


import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.gregetdev.oris.busa.*
import com.gregetdev.oris.busa.R

import com.gregetdev.oris.busa.ViewHolder.ListViewHolder
import com.gregetdev.oris.busa.model.AlarmModel
import com.gregetdev.oris.busa.model.DataImunisasiModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.view_data_imunisasi.view.*


private const val BayiKEY = "param1"


class Fragment_profile : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private var BayiKey_profile: String? = null
    private var NamaBayi: String? = null
    private var status = DataImunisasiModel().status
    private var tanggal = DataImunisasiModel().tgl_imunisasi
    private var tanggal_alarm = AlarmModel().Tgl_alarm


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.retainInstance = true
        arguments?.let {
            BayiKey_profile = it.getString(BayiKEY)
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {return inflater.inflate(R.layout.fragment_profile, container, false)}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val key = BayiKey_profile.toString()
        database = FirebaseDatabase.getInstance()
        val table_bayi = database.getReference("/Bayi")

        table_bayi.child(key).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val profilbayi = dataSnapshot.getValue(BayiModel::class.java)
                NamaBayi = profilbayi?.namaBayi
                Profil_NamaBayiView.text = profilbayi?.namaBayi
                Profil_tglLahriView.text = profilbayi?.tglLahir
                Profil_umurView.text = profilbayi?.umur + " Bulan"
                Profil_JekelView.text = profilbayi?.jekel
                Picasso.get().load(profilbayi?.imageURL).into(Profil_Image)
            }
        })

        Profil_delete_actButton.setOnClickListener(){

            val builder = AlertDialog.Builder(it.context)
                .setTitle("Hapus Data Bayi")
                .setMessage("Apakah Anda Yakin Ingin Menghapus Data Bayi \"$NamaBayi\" ? ")
                .setPositiveButton("HAPUS"){dialog, which -> DeleteProfilBayi(key) }
                .setNegativeButton("TIDAK"){dialog, which -> return@setNegativeButton }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        Profil_edit_actButton.setOnClickListener {
            val intent = Intent(activity, EditDataBayi::class.java)
            intent.putExtra("Key",key)
            startActivity(intent)
        }

        TampilDataImunisasi(key)
    }

    private fun DeleteProfilBayi(key: String) {
        val data_imunisasi = FirebaseDatabase.getInstance().getReference("/Data Imunisasi/$key")
        val data_bayi = FirebaseDatabase.getInstance().getReference("/Bayi/$key")
        val data_alarm = FirebaseDatabase.getInstance().getReference("/Alarm/$key")

        data_bayi.removeValue().addOnCompleteListener {

            data_imunisasi.removeValue()
            data_alarm.removeValue()

            Toast.makeText(context,"Data Bayi dihapus",Toast.LENGTH_SHORT).show()
            val intent = Intent(context, HomeMenu::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun TampilDataImunisasi(key: String) {

        val data_imunisasi = FirebaseDatabase.getInstance().getReference("/Data Imunisasi/$key")
            .orderByChild("umurPemberian")
        val options = FirebaseRecyclerOptions.Builder<DataImunisasiModel>()
            .setQuery(data_imunisasi, DataImunisasiModel::class.java)
            .build()
        val adapterFirebase = object : FirebaseRecyclerAdapter<DataImunisasiModel, ListViewHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ListViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.view_data_imunisasi,parent,false)
                return ListViewHolder(view)
            }
            override fun onBindViewHolder(holder: ListViewHolder, position: Int, model: DataImunisasiModel) {
                holder.mView.Nama_Imunisasi_view.text = model.nama_Imunisasi
                holder.mView.Tgl_imunisasi.text = model.tgl_imunisasi
                if (model.status == "Sudah"){
                    holder.mView.centang_view
                        .setImageResource(R.drawable.ic_check_circle_green_24dp)
                }else {
                    holder.mView.centang_view.visibility = View.INVISIBLE
                }
                holder.mView.setOnClickListener(){
                    QuestionPopUP(model.nama_Imunisasi.toString(),getRef(position).key.toString())
                }
            }

            private fun QuestionPopUP(NamaImunisasi: String, index: String) {
                val builder = AlertDialog.Builder(context!!)
                    .setTitle("Imunisasi")
                    .setMessage("Apakah Imunisasi \"$NamaImunisasi\" Sudah dilakukan ? ")
                    .setPositiveButton("Sudah"){dialog, which ->  SetSudahImunisasi(index)}
                    .setNegativeButton("Belum"){dialog, which -> return@setNegativeButton }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }

            private fun SetSudahImunisasi(index: String) {
                val data_imunisasi = FirebaseDatabase.getInstance()
                    .getReference("/Data Imunisasi/$key/$index")
                data_imunisasi.child(status.toString()).setValue("Sudah")
                    .addOnCompleteListener {
                        Toast.makeText(activity,"Data Imunisasi Disimpan",Toast.LENGTH_LONG).show()
                        DateChange(index)
                    }
                    .addOnCanceledListener { Toast.makeText(activity,"Terjadi kesalahan, Silahkan coba lagi",Toast.LENGTH_LONG).show() }
                startListening()
            }

            private fun DateChange(i: String) {
                val tgl_imunisasi =  FirebaseDatabase.getInstance()
                    .getReference("/Data Imunisasi/$key")
                val tgl_Alarm =  FirebaseDatabase.getInstance()
                    .getReference("/Alarm/$key")
                val index = i.toInt()
                if (index == 1){
                    with(tgl_imunisasi) {
                        child("1").child(tanggal.toString()).setValue(TglImunisasi(0))
                        child("2").child(tanggal.toString()).setValue(TglImunisasi(1))
                        child("3").child(tanggal.toString()).setValue(TglImunisasi(2))
                        child("4").child(tanggal.toString()).setValue(TglImunisasi(3))
                        child("5").child(tanggal.toString()).setValue(TglImunisasi(4))
                        child("6").child(tanggal.toString()).setValue(TglImunisasi(9))
                    }
                    with(tgl_Alarm) {
                        child("1").child(tanggal_alarm).setValue(TglImunisasi(0))
                        child("2").child(tanggal_alarm).setValue(TglImunisasi(1))
                        child("3").child(tanggal_alarm).setValue(TglImunisasi(2))
                        child("4").child(tanggal_alarm).setValue(TglImunisasi(3))
                        child("5").child(tanggal_alarm).setValue(TglImunisasi(4))
                        child("6").child(tanggal_alarm).setValue(TglImunisasi(9))
                    }
                } else if (index == 2){
                    with(tgl_imunisasi) {
                        child("2").child(tanggal.toString()).setValue(TglImunisasi(0))
                        child("3").child(tanggal.toString()).setValue(TglImunisasi(1))
                        child("4").child(tanggal.toString()).setValue(TglImunisasi(2))
                        child("5").child(tanggal.toString()).setValue(TglImunisasi(3))
                        child("6").child(tanggal.toString()).setValue(TglImunisasi(8))
                    }
                    with(tgl_Alarm) {
                        child("2").child(tanggal_alarm).setValue(TglImunisasi(0))
                        child("3").child(tanggal_alarm).setValue(TglImunisasi(1))
                        child("4").child(tanggal_alarm).setValue(TglImunisasi(2))
                        child("5").child(tanggal_alarm).setValue(TglImunisasi(3))
                        child("6").child(tanggal_alarm).setValue(TglImunisasi(8))
                    }
                }else if (index == 3){
                    with(tgl_imunisasi) {
                        child("3").child(tanggal.toString()).setValue(TglImunisasi(0))
                        child("4").child(tanggal.toString()).setValue(TglImunisasi(1))
                        child("5").child(tanggal.toString()).setValue(TglImunisasi(2))
                        child("6").child(tanggal.toString()).setValue(TglImunisasi(7))
                    }
                    with(tgl_Alarm) {
                        child("3").child(tanggal_alarm).setValue(TglImunisasi(0))
                        child("4").child(tanggal_alarm).setValue(TglImunisasi(1))
                        child("5").child(tanggal_alarm).setValue(TglImunisasi(2))
                        child("6").child(tanggal_alarm).setValue(TglImunisasi(7))
                    }
                }else if (index == 4){
                    with(tgl_imunisasi) {
                        child("4").child(tanggal.toString()).setValue(TglImunisasi(0))
                        child("5").child(tanggal.toString()).setValue(TglImunisasi(1))
                        child("6").child(tanggal.toString()).setValue(TglImunisasi(6))
                    }
                    with(tgl_Alarm) {
                        child("4").child(tanggal_alarm).setValue(TglImunisasi(0))
                        child("5").child(tanggal_alarm).setValue(TglImunisasi(1))
                        child("6").child(tanggal_alarm).setValue(TglImunisasi(6))
                    }
                }else if (index == 5){
                    with(tgl_imunisasi) {
                        child("5").child(tanggal.toString()).setValue(TglImunisasi(0))
                        child("6").child(tanggal.toString()).setValue(TglImunisasi(5))
                    }
                    with(tgl_Alarm) {
                        child("5").child(tanggal_alarm).setValue(TglImunisasi(0))
                        child("6").child(tanggal_alarm).setValue(TglImunisasi(5))
                    }
                }else if (index == 6){
                    with(tgl_imunisasi) {
                        child("6").child(tanggal.toString()).setValue(TglImunisasi(0))
                    }
                    with(tgl_Alarm) {
                        child("6").child(tanggal_alarm).setValue(TglImunisasi(0))
                    }
                }
            }
            private fun TglImunisasi(tambah: Long): String {
                val calendar = Calendar.getInstance()
                var Year = calendar.get(Calendar.YEAR)
                var Months = calendar.get(Calendar.MONTH) + 1
                var Day = calendar.get(Calendar.DAY_OF_MONTH)

                var MonthsPlus = Months + tambah
                var DaysPlus: String

                if (Day in 1..9){ DaysPlus = "0$Day"
                } else { DaysPlus = "$Day"}

                if (MonthsPlus > 12){
                    MonthsPlus -= 12
                    Year += 1
                    if(MonthsPlus in 1..9) {
                        Log.d("Calendar","$Day/0$MonthsPlus/$Year")
                        return "$DaysPlus/0$MonthsPlus/$Year"
                    } else {
                        Log.d("Calendar","$Day/$MonthsPlus/$Year")
                        return "$DaysPlus/$MonthsPlus/$Year"
                    }
                } else {
                    if(MonthsPlus in 1..9) {
                        Log.d("Calendar","$Day/0$MonthsPlus/$Year")
                        return "$DaysPlus/0$MonthsPlus/$Year"
                    } else {
                        Log.d("Calendar","$Day/$MonthsPlus/$Year")
                        return "$DaysPlus/$MonthsPlus/$Year"
                    }
                }
            }



        }
        adapterFirebase.startListening()
        data_imunisasi_recylerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        data_imunisasi_recylerView.adapter = adapterFirebase

    }

    companion object {

        @JvmStatic
        fun newInstance(Key: String) =
            Fragment_profile().apply {
                arguments = Bundle().apply {
                    putString(BayiKEY, Key)
                }

            }
    }
}



