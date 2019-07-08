package com.gregetdev.oris.busa

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.gregetdev.oris.busa.LoginScreen.Login
import com.race604.drawable.wave.WaveDrawable





class SplashScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        auth = FirebaseAuth.getInstance()

        IsUserLogin()
    }

    private fun IsUserLogin() {
        val uid = auth.uid
        val imageView = findViewById(R.id.LogoSplash) as ImageView
        val mWaveDrawable = WaveDrawable(imageView.getDrawable())
        mWaveDrawable.isIndeterminate = true
        mWaveDrawable.setWaveAmplitude(20)
        imageView.setImageDrawable(mWaveDrawable)
        if(uid != null){
            val time = object :Thread(){
                override fun run() {
                    try {
                        Thread.sleep(4000)
                        Log.d("Splash","Splash Finish")
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    } finally {
                        val intent = Intent(this@SplashScreen, HomeMenu::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
            time.start()


            }else{
            val time = object : Thread(){
                 override fun run() {
                    try {
                        Thread.sleep(4000)
                        Log.d("Splash","Splash Finish")
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    } finally {
                        val intent = Intent(this@SplashScreen, Login::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
            time.start()

        }

        }

    }

