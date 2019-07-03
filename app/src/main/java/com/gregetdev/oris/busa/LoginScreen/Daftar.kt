package com.gregetdev.oris.busa.LoginScreen

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.gregetdev.oris.busa.HomeMenu
import com.gregetdev.oris.busa.R
import com.gregetdev.oris.busa.model.UserModel
import kotlinx.android.synthetic.main.activity_daftar.*

class Daftar : AppCompatActivity(){


    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar)

        auth = FirebaseAuth.getInstance()


        daftar.setOnClickListener(){

            var checkConnetion = verifyAvailableNetwork(this@Daftar)
            if (checkConnetion == true){
                signUp_user()
            } else {
                Toast.makeText(this@Daftar, "Tidak tersambung\nPeriksa Koneksi Internet Anda",
                    Toast.LENGTH_SHORT).show()
            }

        }

        BackButton.setOnClickListener(){
            startActivity(Intent(this@Daftar, Login::class.java))
            finish()}
    }

    private fun verifyAvailableNetwork(activity:AppCompatActivity):Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }

    private fun signUp_user() {
        if(daftarNama_tv.text.toString().isEmpty()){
            daftarNama_tv.error = "Masukan Nama"
            daftarNama_tv.requestFocus()
            return
        }
        if(daftarEmail_tv.text.toString().isEmpty()){
            daftarEmail_tv.error = "Masukan Nomor Telepon"
            daftarEmail_tv.requestFocus()
            return
        }
        if(daftarPass_tv.text.toString().isEmpty()){
            daftarPass_tv.error = "Masukan Email"
            daftarPass_tv.requestFocus()
            return

        }
        auth.createUserWithEmailAndPassword(daftarEmail_tv.text.toString(), daftarPass_tv.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@Daftar,"Berhasil Membuat User",Toast.LENGTH_LONG).show()
                    Log.d("Regist","Create user success")
                    SaveUserToFirebaseDatabase()
                }
            }.addOnFailureListener(){
                Toast.makeText(baseContext, "${it.message}",
                    Toast.LENGTH_SHORT).show()
                    daftarEmail_tv.text.clear()
                    daftarPass_tv.text.clear()
                    daftarNama_tv.text.clear()
            }
    }

    private fun SaveUserToFirebaseDatabase(){
        val uid = auth.uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/User/$uid")
        val user = UserModel(uid,daftarNama_tv.text.toString(),daftarEmail_tv.text.toString())
        ref.setValue(user)
            .addOnCompleteListener(){
                Log.d("Regist","Save User to firebase database success")
                // Membuat saat menekan back button tidak kembali ke halaman Register
                val intent = Intent(this@Daftar, HomeMenu::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }.addOnFailureListener(){
                Log.d("Regist","${it.message}")
            }
    }


}
