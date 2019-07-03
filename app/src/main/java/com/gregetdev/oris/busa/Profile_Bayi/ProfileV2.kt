package com.gregetdev.oris.busa.Profile_Bayi

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.gregetdev.oris.busa.BayiModel
import com.gregetdev.oris.busa.R
import com.gregetdev.oris.busa.model.DataImunisasiModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile_v2.*
import kotlinx.android.synthetic.main.view_data_imunisasi.view.*

class ProfileV2 : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_v2)

        val key = intent.getStringExtra("BayiKey").toString()
        database = FirebaseDatabase.getInstance()
        val table_bayi = database.getReference("/Bayi")

        table_bayi.child(key).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {

            }

            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val profilbayi = dataSnapshot.getValue(BayiModel::class.java)

                Profil_NamaBayiView.text = profilbayi?.namaBayi
                Profil_tglLahriView.text = profilbayi?.tglLahir
                Profil_umurView.text = profilbayi?.umur + " Bulan"
                Profil_JekelView.text = profilbayi?.jekel

                Picasso.get().load(profilbayi?.imageURL).into(Profil_Image)

            }
        })

        Button_Reminder.setOnClickListener{
            val intent = Intent(this@ProfileV2, Alarm::class.java)
            intent.putExtra("BayiKey",key)
            startActivity(intent)
        }

        Button_Calendar.setOnClickListener(){
            val intent = Intent(this@ProfileV2, Calendar::class.java)
            intent.putExtra("BayiKey",key)
            startActivity(intent)
        }

        /*TampilDataImunisasi(key)*/

    }

   /* private fun TampilDataImunisasi(key: String) {

        val data_imunisasi = FirebaseDatabase.getInstance().getReference("/Data Imunisasi/$key")
            .orderByChild("umurPemberian")


        val options = FirebaseRecyclerOptions.Builder<DataImunisasiModel>()
            .setQuery(data_imunisasi, DataImunisasiModel::class.java)
            .build()

        val adapterFirebase = object : FirebaseRecyclerAdapter<DataImunisasiModel, ListDataImunisasiHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ListDataImunisasiHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.view_data_imunisasi,parent,false)
                return ListDataImunisasiHolder(view)
            }

            override fun onBindViewHolder(holder: ListDataImunisasiHolder, position: Int, model: DataImunisasiModel) {

                holder.mView.Nama_Imunisasi_view.text = getRef(position).key.toString()
                holder.mView.Tgl_imunisasi.text = model.Tgl_imunisasi

                if (model.Keterangan == "Sudah"){
                    holder.mView.centang_view.setImageResource(R.drawable.ic_check_circle_green_24dp)
                }else {
                    holder.mView.centang_view.visibility = View.INVISIBLE
                }

                holder.mView.setOnClickListener(){

                }

            }

            override fun startListening() {
                super.startListening()
            }
        }
        adapterFirebase.startListening()
        data_imunisasi_recylerViewV2.layoutManager = LinearLayoutManager(this@ProfileV2,LinearLayoutManager.VERTICAL,false)
        data_imunisasi_recylerViewV2.adapter = adapterFirebase

    }*/


}

