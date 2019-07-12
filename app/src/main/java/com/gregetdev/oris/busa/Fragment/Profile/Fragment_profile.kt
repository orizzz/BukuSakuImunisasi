package com.gregetdev.oris.busa.Fragment.Profile


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.gregetdev.oris.busa.*

import com.gregetdev.oris.busa.ViewHolder.ListViewHolder
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
                holder.mView.Nama_Imunisasi_view.text = getRef(position).key.toString()
                holder.mView.Tgl_imunisasi.text = model.Tgl_imunisasi
                if (model.Keterangan == "Sudah"){
                    holder.mView.centang_view
                        .setImageResource(R.drawable.ic_check_circle_green_24dp)
                }else {
                    holder.mView.centang_view.visibility = View.INVISIBLE
                }
                holder.mView.setOnClickListener(){
                    QuestionPopUP(getRef(position).key.toString())
                }
            }

            private fun QuestionPopUP(NamaImunisasi: String) {
                val builder = AlertDialog.Builder(context!!)
                    .setTitle("Imunisasi")
                    .setMessage("Apakah Imunisasi \"$NamaImunisasi\" Sudah dilakukan ? ")
                    .setPositiveButton("Sudah"){dialog, which ->  SetSudahImunisasi(NamaImunisasi)}
                    .setNegativeButton("Belum"){dialog, which -> return@setNegativeButton }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }

            private fun SetSudahImunisasi(namaImunisasi: String) {
                val data_imunisasi = FirebaseDatabase.getInstance()
                    .getReference("/Data Imunisasi/$key/$namaImunisasi")
                data_imunisasi.child("keterangan").setValue("Sudah")
                    .addOnCompleteListener { Toast.makeText(activity,"Data Imunisasi Disimpan",Toast.LENGTH_LONG).show() }
                    .addOnCanceledListener { Toast.makeText(activity,"Terjadi kesalahan, Silahkan coba lagi",Toast.LENGTH_LONG).show() }
                startListening()
            }

            override fun startListening() {
                super.startListening()
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



