package com.gregetdev.oris.busa.LoginScreen

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.gregetdev.oris.busa.Home
import com.gregetdev.oris.busa.R.layout.activity_login
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_login)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()


        //val daftarBtn = findViewById<>(R.id.daftarButton) as Button

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

                    startActivity(Intent(this, Home::class.java))
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    finish()
                }
            }.addOnFailureListener(this){
                Login_loading.visibility = View.GONE
                Loginbutton.visibility = View.VISIBLE
                Toast.makeText(baseContext, "Email atau password Salah",
                    Toast.LENGTH_SHORT).show()
            }

        var CurrentUser = auth.currentUser
        CurrentUser?.let{
            Log.d("Login","UID : ${CurrentUser.uid}")
            Log.d("Login","Token : ${CurrentUser.displayName}")
        }

/*
        val HomeIntent = Intent(this@Login,Home::class.java)

        val ref = FirebaseDatabase.getInstance().getReference("User")

        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var user : UserModel? = dataSnapshot.child(EmailLogin.text.toString()).getValue(UserModel::class.java)
                if(user.Password.equals(PasswordLogin.text.toString())){
                    startActivity(HomeIntent)

                    finish()
                }
            }

        })
*/





    }


}
