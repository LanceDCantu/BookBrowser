package com.example.lance.bookbrowser.MyOffers

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import com.example.lance.bookbrowser.*
import com.example.lance.bookbrowser.Cart.Cart
import com.example.lance.bookbrowser.MyInterests.MyOffersFragment
import com.example.lance.bookbrowser.StoreLocater.StoreLocater
import kotlinx.android.synthetic.main.activity_my_offers.*
import com.example.lance.bookbrowser.UserData


class MyOffers : AppCompatActivity() {

    val myUser = UserData.getData()

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
        setContentView(R.layout.activity_my_offers)

        navigation_offers.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_offers)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        bottomNavigation.getMenu().findItem(R.id.navigation_user_market).setChecked(true)

        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            val fragment = MyOffersFragment()
            transaction.replace(R.id.sample_content_fragment, fragment)
            transaction.commit()
        }

        val add_button = findViewById<FloatingActionButton>(R.id.add_fab)

        // set on-click listener
        add_button.setOnClickListener {
            val intent = Intent(this, AddOffer::class.java)
            startActivity(intent)
        }
    }
}
