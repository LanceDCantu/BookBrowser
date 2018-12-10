package com.example.lance.bookbrowser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.constraint.Group
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity

import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import com.example.lance.bookbrowser.notification.NotificationCountSetClass
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

import com.google.firebase.database.*
import com.google.firebase.database.core.Constants

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private val ref = FirebaseDatabase.getInstance().reference

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.button2->{
                cartItems++
                NotificationCountSetClass.setNotifyCount(cartItems)
                invalidateOptionsMenu() //refresh counter
            }

            R.id.button3->{
                cartItems--
                NotificationCountSetClass.setNotifyCount(cartItems)
                invalidateOptionsMenu() //refresh counter
            }

        }
    }

    var cartItems = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //val test_string = "Price"

        //val newItem = ref.child(test_string).push()
        //then, we used the reference to set the value on that ID
        //newItem.setValue(2.14)

        val book1btn = findViewById<Button>(R.id.book_1_button)
        book1btn?.setOnClickListener {
            Toast.makeText(this, "we did it!", Toast.LENGTH_SHORT).show()
        }

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

        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.app_bar_search).actionView as SearchView).apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        var item = menu.findItem(R.id.action_cart)
        NotificationCountSetClass.setAddToCart(this@MainActivity, item, cartItems!!)
        return super.onPrepareOptionsMenu(menu)
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
            R.id.nav_login -> {
                val intent = Intent(this, login::class.java)
                //intent.putExtra("keyIdentifier", value)
                startActivity(intent)
            }
            R.id.nav_logout -> {
                finish()  // This call is missing.
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
