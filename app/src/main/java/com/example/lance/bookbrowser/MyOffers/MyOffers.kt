package com.example.lance.bookbrowser.MyOffers

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.lance.bookbrowser.*
import com.example.lance.bookbrowser.Cart.Cart
import com.example.lance.bookbrowser.MyInterests.MyOffersFragment
import kotlinx.android.synthetic.main.activity_my_offers.*
import kotlinx.android.synthetic.main.activity_store_locater.*


class MyOffers : AppCompatActivity() {

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


        val offer_button = findViewById<Button>(R.id.book_button_offers)

        // set on-click listener
        offer_button.setOnClickListener {
            Toast.makeText(this, "Book Clicked!", Toast.LENGTH_LONG).show()
            val intent = Intent(this, BookInfoMarket::class.java)
            intent.putExtra("book_isbn", "9789402306538")
            intent.putExtra("market_id", "-L_6dO9oK5DLIC4ZVzcX")
            intent.putExtra("is_owner", true)
            startActivity(intent)
        }
    }
}
