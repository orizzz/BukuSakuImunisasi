package com.gregetdev.oris.busa

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.gregetdev.oris.busa.ViewHolder.ListViewHolder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_data_bayi.*
import kotlinx.android.synthetic.main.view_identitas_bayi.view.*

class DataBayi : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_bayi)

        add_dataBayi.setOnClickListener {
            val intentAddData = Intent(this@DataBayi,AddDataBayi::class.java)
            startActivity(intentAddData)
        }

        DisplayDataBayi()
    }

    private fun DisplayDataBayi(){

        val parentID = FirebaseAuth.getInstance().uid.toString()
        Log.d("parentID","$parentID")

        val Table_bayi = FirebaseDatabase.getInstance().getReference("/Bayi")
            .orderByChild("parentID").equalTo(parentID)


        val options = FirebaseRecyclerOptions.Builder<BayiModel>()
            .setQuery(Table_bayi, BayiModel::class.java)
            .build()

        val adapterFirebase = object : FirebaseRecyclerAdapter<BayiModel, ListViewHolder>(options){

            override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ListViewHolder
            {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.view_identitas_bayi,parent,false)
                return ListViewHolder(view)

            }

            override fun onBindViewHolder(holder: ListViewHolder, position: Int, model: BayiModel) {

                holder.mView.Home_NamaBayiView.text = model.namaBayi
                holder.mView.Home_tglLahriView.text = model.tglLahir
                holder.mView.Home_umurView.text = model.umur
                holder.mView.Home_JekelView.text = model.jekel
                Picasso.get().load(model.imageURL).into(holder.mView.Home_Profil_imageView)

                holder.mView.setOnClickListener(){
                    val key = getRef(position).key.toString()
                    val intent = Intent(this@DataBayi, Profile::class.java)
                    intent.putExtra("BayiKey",key)

                    Log.d("Recylerview","${getRef(position).key}")
                    Log.d("BayiKey","Di cardview : $key")
                    startActivity(intent)
                }
            }

            override fun startListening() {
                super.startListening()
            }



        }
        adapterFirebase.startListening()
        Bayi_RecylerView.layoutManager = LinearLayoutManager(this@DataBayi, LinearLayoutManager.VERTICAL,false)
        Bayi_RecylerView.adapter = adapterFirebase
    }
}

