package com.example.lance.bookbrowser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity

import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

import com.google.firebase.database.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {

    private val ref = FirebaseDatabase.getInstance().getReference("products")
    private val database = Database()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            val fragment = RecyclerViewFragment()
            transaction.replace(R.id.sample_content_fragment, fragment)
            transaction.commit()
        }
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        onNewIntent(intent)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        (findViewById<SearchView>(R.id.search_bar)).apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
        }

        return true
    }


    override fun onNewIntent(intent: Intent?) {
        this.intent = intent
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        Log.e("SearchableActivity", "handleIntent called " + intent?.action)
        if (Intent.ACTION_SEARCH == intent?.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                doMySearch(query)
            }
        }
    }

    private fun doMySearch(query: String) {
        //val books = database.searchBooks(query)
        //val adapter = SearchAdapter(this, books)
        //val listView = findViewById<ListView>(R.id.listView)
        Toast.makeText(this, "we did it!", Toast.LENGTH_SHORT).show()
        //listView.adapter = adapter
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.e("MainActivity", "Navidation item selected " + item.itemId)
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_stores -> {
                // Handle the camera action
                val intent = Intent(this, StoreLocater::class.java)
                //intent.putExtra("keyIdentifier", value)
                startActivity(intent)
            }
            R.id.nav_orders -> {
                val intent = Intent(this, Cart::class.java)
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
                finish()  // This call is missing.
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        Log.e("MainActivity", "Option item selected " + item?.itemId)
        when(item?.itemId)
        {
            R.id.action_cart -> {
                val intent = Intent(this, Cart::class.java)
                //intent.putExtra("keyIdentifier", value)
                startActivity(intent)
                return true
            }
            else -> {
               return super.onOptionsItemSelected(item)
            }
        }
    }
}