package com.example.lance.bookbrowser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main_bn.*


class MainActivity : AppCompatActivity() {

    lateinit var toolbar: ActionBar

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_stores -> {
                val storeFragment = StoreLocater()
                openFragment(storeFragment)
                //message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_user_market -> {
                //message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_browse -> {
                val recyclerViewFragment = RecyclerViewFragment(Array(60) { Book(1, "none", "none",0.0, 0) })
                openFragment(recyclerViewFragment)
                //message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_cart -> {
                val cartFragment = Cart()
                openFragment(cartFragment)
                //message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_account -> {
                val myAcountFragment = MyAccount()
                openFragment(myAcountFragment)
                //message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_bn)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if(supportActionBar != null) {
            toolbar = supportActionBar as ActionBar
        }

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        onNewIntent(intent)
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.sample_content_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
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
}
