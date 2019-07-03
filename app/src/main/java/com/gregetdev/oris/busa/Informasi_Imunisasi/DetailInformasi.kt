package com.gregetdev.oris.busa.Informasi_Imunisasi

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout.JUSTIFICATION_MODE_INTER_WORD
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.gregetdev.oris.busa.R
import com.gregetdev.oris.busa.model.InfoModel
import kotlinx.android.synthetic.main.activity_detail_informasi.*

class DetailInformasi : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_informasi)

        val key = intent.getStringExtra("InfoKEY").toString()

        val Table_info = FirebaseDatabase.getInstance().getReference("/Informasi_imunisasi")
        Table_info.child(key).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val detail = dataSnapshot.getValue(InfoModel::class.java)

                Detail_nama_imunisasi.text = detail?.Nama_Imunisasi.toString()
                Detail_Deskripsi.text = detail?.Deskripsi.toString()
                Detail_Manfaat.text = detail?.Manfaat.toString()
                Detail_Efek_samping.text = detail?.EfekSamping.toString()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Detail_Deskripsi.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD)
                    Detail_Manfaat.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD)
                    Detail_Efek_samping.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD)
                };

            }

        })
    }
}
