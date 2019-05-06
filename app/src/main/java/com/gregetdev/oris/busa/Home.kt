package com.gregetdev.oris.busa

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.gregetdev.oris.busa.Fragment.Home.Fragment_Home
import com.gregetdev.oris.busa.Fragment.Home.Fragment_informasi
import com.gregetdev.oris.busa.LoginScreen.Login
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*


class Home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Home"

        auth = FirebaseAuth.getInstance()


        NavigationViewDisplay(-1)
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)



    }



    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_signOut  -> {
                auth.signOut()
                val intent = Intent(this@Home, Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
            return super.onOptionsItemSelected(item)
    }

    private fun NavigationViewDisplay(id : Int){
        val fragment = when(id){
            R.id.nav_data_bayi -> {
                Fragment_Home()
            }
            R.id.nav_informasi ->{
                Fragment_informasi()
            }
            else ->{
                Fragment_Home()
            }
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.Content_layout, fragment)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle profile_navigation view item clicks here.
        /*when (item.itemId) {
            R.id.nav_data_bayi -> {
            }
        }*/
        NavigationViewDisplay(item.itemId)

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

}

