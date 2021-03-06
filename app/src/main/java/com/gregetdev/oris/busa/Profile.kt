package com.gregetdev.oris.busa

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import com.gregetdev.oris.busa.Fragment.Profile.Fragment_Alarm
import com.gregetdev.oris.busa.Fragment.Profile.Fragment_Remider
import com.gregetdev.oris.busa.Fragment.Profile.Fragment_profile
import kotlinx.android.synthetic.main.activity_profile.*

class Profile : AppCompatActivity() {

    private lateinit var ProfileBottomNav: BottomNavigationView
    private lateinit var ProfileContent: FrameLayout

    private lateinit var bayiKEY : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        ProfileContent = findViewById(R.id.Profil_content)


        bayiKEY = intent.getStringExtra("BayiKey")
        Log.d("BayiKey","profil : $bayiKEY")

        val actionBar = supportActionBar
        actionBar!!.setTitle("Profile")

        val frag_profile = Fragment_profile.newInstance(bayiKEY)
        val frag_reminder = Fragment_Remider.newInstance(bayiKEY)
        val frag_alarm = Fragment_Alarm.newInstance(bayiKEY)
        setFragment(frag_profile)
        navigation.setOnNavigationItemSelectedListener(
            object : BottomNavigationView.OnNavigationItemSelectedListener {
                @SuppressLint("ResourceAsColor")
                override fun onNavigationItemSelected(item: MenuItem): Boolean {
                    when (item.itemId) {
                        R.id.navigation_Profil_bayi -> {
                            setFragment(frag_profile)
                            actionBar.setTitle("Profil Bayi")
                            return true
                        }
                        R.id.navigation_Calendar -> {
                            setFragment(frag_reminder)
                            actionBar.setTitle("Calendar")
                            return true
                        }

                        R.id.navigation_notif -> {
                            setFragment(frag_alarm)
                            actionBar.setTitle("Alarm")
                            return true
                        }
                    }
                    return false
                }

            })
    }


    private fun setFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.Profil_content,fragment)
        fragmentTransaction.commit()
    }




}
