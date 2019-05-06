package com.gregetdev.oris.busa.Fragment.Home

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_home.*
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log

import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.gregetdev.oris.busa.*
import com.gregetdev.oris.busa.Fragment.Profile.Fragment_profile
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_identitas_bayi.view.*

class Fragment_Home : Fragment() {

    //var bayi : List<BayiModel>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        add_dataBayi.setOnClickListener {
            val intentAddData = Intent(activity,AddDataBayi::class.java)
            startActivity(intentAddData)
        }

        DisplayDataBayi()
    }

    companion object {
        val Bayi_Key = "Bayi_key"
    }

    private fun DisplayDataBayi(){

        val parentID = FirebaseAuth.getInstance().uid.toString()
        Log.d("parentID","$parentID")

        val Table_bayi = FirebaseDatabase.getInstance().getReference("/Bayi")
            .orderByChild("parentID").equalTo(parentID)


        val options = FirebaseRecyclerOptions.Builder<BayiModel>()
            .setQuery(Table_bayi, BayiModel::class.java)
            .build()

        val adapterFirebase = object : FirebaseRecyclerAdapter<BayiModel, ListBayiHolder>(options){

            override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ListBayiHolder
            {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.view_identitas_bayi,parent,false)
                return ListBayiHolder(view)

            }

            override fun onBindViewHolder(holder: ListBayiHolder, position: Int, model: BayiModel) {

                    holder.mView.Home_NamaBayiView.text = model.namaBayi
                    holder.mView.Home_tglLahriView.text = model.tglLahir
                    holder.mView.Home_umurView.text = model.umur
                    holder.mView.Home_JekelView.text = model.jekel
                    Picasso.get().load(model.imageURL).into(holder.mView.Home_Profil_imageView)

                holder.mView.setOnClickListener(){
                   /* val intent = Intent(it.context,Profilbayi::class.java)*/
                    val intent = Intent(it.context,Profile::class.java)
                    val key = getRef(position).key.toString()
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
        Bayi_RecylerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        Bayi_RecylerView.adapter = adapterFirebase
    }


}

class ListBayiHolder(var mView: View) : RecyclerView.ViewHolder(mView)