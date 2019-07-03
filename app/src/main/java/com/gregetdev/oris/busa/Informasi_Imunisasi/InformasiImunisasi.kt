package com.gregetdev.oris.busa.Informasi_Imunisasi

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.gregetdev.oris.busa.R
import com.gregetdev.oris.busa.ViewHolder.ListViewHolder
import com.gregetdev.oris.busa.model.InfoModel
import kotlinx.android.synthetic.main.activity_informasi_imunisasi.*
import kotlinx.android.synthetic.main.view_informasi_imunisasi.view.*

class InformasiImunisasi : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informasi_imunisasi)

        TampilListInformasi()

    }

    private fun TampilListInformasi() {
        val Table_info = FirebaseDatabase.getInstance().getReference("/Informasi_imunisasi")

        val options = FirebaseRecyclerOptions.Builder<InfoModel>()
            .setQuery(Table_info, InfoModel::class.java)
            .build()

        val adapterFirebase = object : FirebaseRecyclerAdapter<InfoModel, ListViewHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ListViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.view_informasi_imunisasi,parent,false)
                return ListViewHolder(view)
            }

            override fun onBindViewHolder(holder: ListViewHolder, position: Int, model: InfoModel) {

                holder.mView.Nama_Info_Imunisasi.text = model.Nama_Imunisasi

                holder.mView.setOnClickListener{
                    val key = getRef(position).key.toString()
                    val intent = Intent(this@InformasiImunisasi, DetailInformasi::class.java)
                    intent.putExtra("InfoKEY",key)
                    startActivity(intent)
                }

            }




        }

        adapterFirebase.startListening()
        ListInfo.layoutManager = LinearLayoutManager(this@InformasiImunisasi, LinearLayoutManager.VERTICAL,false)
        ListInfo.adapter = adapterFirebase
    }
}
