package com.example.lance.bookbrowser.SellerCustomerChooser

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import com.example.lance.bookbrowser.Cart.Cart
import com.example.lance.bookbrowser.MainSearchActivity
import com.example.lance.bookbrowser.MarketDirectory
import com.example.lance.bookbrowser.MyAccount
import com.example.lance.bookbrowser.R
import com.example.lance.bookbrowser.StoreLocater.StoreLocater
import kotlinx.android.synthetic.main.activity_messaging.*
import kotlinx.android.synthetic.main.activity_seller_customer_chooser.*

class SellerCustomerChooserActivity : AppCompatActivity() {

    lateinit var market_id: String

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
        setContentView(R.layout.activity_seller_customer_chooser)

        market_id = intent.getStringExtra("id")

        chooser_activity_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.chooser_activity_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        bottomNavigation.getMenu().findItem(R.id.navigation_user_market).setChecked(true)

        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            val fragment = SellerCustomerChooserFragment()
            val args = Bundle()
            args.putString("id", market_id)
            fragment.arguments = args
            transaction.replace(R.id.sample_content_fragment, fragment)
            transaction.commit()
        }
    }
}
