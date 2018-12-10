package com.example.lance.bookbrowser

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

import com.google.firebase.database.*
import com.google.firebase.database.core.Constants

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val ref = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val test_string = "Price"

        val newItem = ref.child(test_string).push()
        //then, we used the reference to set the value on that ID
        newItem.setValue(2.14)

        toolbar.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
            )
            drawer_layout.addDrawerListener(toggle)
            toggle.syncState()




            nav_view.setNavigationItemSelectedListener(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_stores -> {
                // Handle the camera action
                val intent = Intent(this, StoreLocater::class.java)
                //intent.putExtra("keyIdentifier", value)
                startActivity(intent)
            }
            R.id.nav_orders -> {
                val intent = Intent(this, cart::class.java)
                //intent.putExtra("keyIdentifier", value)
                startActivity(intent)
            }
            R.id.nav_options -> {

            }
            R.id.nav_about_us -> {
                val intent = Intent(this, AboutUs::class.java)
                //intent.putExtra("keyIdentifier", value)
                startActivity(intent)
            }
            R.id.nav_logout -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
