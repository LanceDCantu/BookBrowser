package com.example.lance.bookbrowser

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.example.lance.bookbrowser.Cart.Cart
import com.example.lance.bookbrowser.MyInterests.MyInterests
import com.example.lance.bookbrowser.MyOffers.MyOffers
import com.example.lance.bookbrowser.StoreLocater.StoreLocater
import kotlinx.android.synthetic.main.market_directory.*

class MarketDirectory : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_stores -> {
                val intent = Intent(this, StoreLocater::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_user_market -> {
                val intent = Intent(this, MarketDirectory::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_browse -> {
                val intent = Intent(this, MainSearchActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_cart -> {
                val intent = Intent(this, Cart::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_account -> {
                val intent = Intent(this, MyAccount::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.market_directory)

        navigation_market_directory.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_market_directory)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        bottomNavigation.getMenu().findItem(R.id.navigation_user_market).setChecked(true)

        initButtons()
    }

    private fun initButtons() {
        val explore_button = findViewById<Button>(R.id.explore_button)
        val my_interests_button = findViewById<Button>(R.id.my_interests_button)
        val my_offers_button = findViewById<Button>(R.id.my_offers_button)

        // set on-click listener
        explore_button.setOnClickListener {
            val intent = Intent(this, MarketSearchActivity::class.java)
            startActivity(intent)
        }

        // set on-click listener
        my_interests_button.setOnClickListener {
            val intent = Intent(this, MyInterests::class.java)
            startActivity(intent)
        }

        // set on-click listener
        my_offers_button.setOnClickListener {
            val intent = Intent(this, MyOffers::class.java)
            startActivity(intent)
        }
    }
}


