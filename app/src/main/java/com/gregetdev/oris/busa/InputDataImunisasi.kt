package com.gregetdev.oris.busa

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_input_data_imunisasi.*

class InputDataImunisasi : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_data_imunisasi)

        val namaImunisasi = intent.getStringExtra("Imunisasi")
        val bayiKEY = intent.getStringExtra("BayiKey")

        Log.d("Intent data","$namaImunisasi dan $bayiKEY")

        database = FirebaseDatabase.getInstance()
        val dataImunisasi = database.getReference("/Data Imunisasi/$bayiKEY/$namaImunisasi")

        dataImunisasi.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
              Dialog_NamaImunisasi.text = dataSnapshot.key
            }
        })
    }
}
