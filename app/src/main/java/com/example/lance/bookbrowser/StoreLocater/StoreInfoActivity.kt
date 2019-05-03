package com.example.lance.bookbrowser.StoreLocater

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.View
import android.widget.TextView
import com.example.lance.bookbrowser.Cart.Cart
import com.example.lance.bookbrowser.MainSearchActivity
import com.example.lance.bookbrowser.MarketDirectory
import com.example.lance.bookbrowser.MyAccount
import com.example.lance.bookbrowser.R
import com.example.lance.bookbrowser.R.id.navigation_store_info
import kotlinx.android.synthetic.main.activity_store_info.*

class StoreInfoActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_store_info)

        navigation_store_info.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_store_info)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        bottomNavigation.getMenu().findItem(R.id.navigation_stores).setChecked(true)

        val website_button = findViewById<TextView>(R.id.website_URL)

        website_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(website_URL.text as String?))
                startActivity(browserIntent)
            }
        })

        //Look in to the automatic calling functionality
        //Look into clicking the website link and going directly
    }
}
