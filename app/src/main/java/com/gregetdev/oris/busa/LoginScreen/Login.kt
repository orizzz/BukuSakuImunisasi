package com.gregetdev.oris.busa.LoginScreen

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.gregetdev.oris.busa.HomeMenu
import com.gregetdev.oris.busa.R.layout.activity_login
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_login)
        auth = FirebaseAuth.getInstance()


        daftarButton.setOnClickListener(){
            startActivity(Intent(this, Daftar::class.java))
            finish()
        }

        Loginbutton.setOnClickListener(){
            var checkConnetion = verifyAvailableNetwork(this@Login)
            if (checkConnetion == true){
                loginUser()
            } else {

                Toast.makeText(this@Login, "Tidak tersambung\nPeriksa Koneksi Internet Anda",
                    Toast.LENGTH_SHORT).show()
            }

        }




    }
    private fun verifyAvailableNetwork(activity:AppCompatActivity):Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }

    private fun loginUser() {
        if(EmailLogin.text.toString().isEmpty()){
            EmailLogin.error = "Masukan Nomor Telepon"
            EmailLogin.requestFocus()
            return
        }
        if(PasswordLogin.text.toString().isEmpty()){
            PasswordLogin.error = "Masukan Email"
            PasswordLogin.requestFocus()
            return

        }
        Login_loading.visibility = View.VISIBLE
        Loginbutton.visibility = View.INVISIBLE
        auth.signInWithEmailAndPassword(EmailLogin.text.toString(), PasswordLogin.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    startActivity(Intent(this, HomeMenu::class.java))
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    finish()
                }
            }.addOnFailureListener(this){
                Login_loading.visibility = View.GONE
                Loginbutton.visibility = View.VISIBLE
                Toast.makeText(baseContext, "Email atau password Salah",
                    Toast.LENGTH_SHORT).show()
            }


    }


}
