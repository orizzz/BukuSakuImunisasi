package com.gregetdev.oris.busa

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_cara_penggunaan.*

class CaraPenggunaan : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cara_penggunaan)

        val actionBar = supportActionBar
        actionBar!!.setTitle("Cara Penggunaan Aplikasi")

        exp_t_tambah_data.setOnClickListener {
            if(expandable_01.isExpanded){expandable_01.collapse()}
            else{expandable_01.expand()
                expandable_02.collapse()
                expandable_03.collapse()}
        }
        exp_t_imunisasi.setOnClickListener {
            if(expandable_02.isExpanded){expandable_02.collapse()}
            else{
                expandable_02.expand()
                expandable_01.collapse()
                expandable_03.collapse()
            }
        }
        exp_t_hapus.setOnClickListener {
            if(expandable_03.isExpanded){expandable_03.collapse()}
            else{
                expandable_03.expand()
                expandable_01.collapse()
                expandable_02.collapse()
            }
        }
    }
}
