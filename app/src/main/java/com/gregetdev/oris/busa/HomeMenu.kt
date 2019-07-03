package com.gregetdev.oris.busa

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.gregetdev.oris.busa.Informasi_Imunisasi.InformasiImunisasi
import com.gregetdev.oris.busa.LoginScreen.Login
import kotlinx.android.synthetic.main.activity_home_menu.*
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_about.*
import kotlinx.android.synthetic.main.dialog_about.view.*


class HomeMenu : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_menu)
        auth = FirebaseAuth.getInstance()
        Home_Data_Bayi.setOnClickListener(){
            val intent = Intent(this@HomeMenu, DataBayi::class.java)
            Log.d("Home","Data Bayi")
            startActivity(intent)
        }

        Home_Log_out.setOnClickListener{
            auth.signOut()
            val intent = Intent(this@HomeMenu, Login::class.java)
            startActivity(intent)
            finish()
        }
        Home_Informasi.setOnClickListener(){
            val intent = Intent(this@HomeMenu, InformasiImunisasi::class.java)
            Log.d("Home","Informasi Imunisasi")
            startActivity(intent)
        }

        Home_About.setOnClickListener{
            AboutDialog()
        }

        Home_Cara_pemakaian.setOnClickListener{
            val intent = Intent(this@HomeMenu, CaraPenggunaan::class.java)
            Log.d("Home","Cara Penggunaan")
            startActivity(intent)
        }
    }

    private fun AboutDialog() {

        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_about, viewGroup, false)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        val alertDialog = builder.create()
        alertDialog.show()
        dialogView.About_kembali_buttom.setOnClickListener{
            alertDialog.dismiss()
        }

    }
}
