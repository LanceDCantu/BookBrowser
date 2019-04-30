package com.example.lance.bookbrowser.messaging

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.lance.bookbrowser.*
import com.example.lance.bookbrowser.Cart.Cart
import kotlinx.android.synthetic.main.activity_my_interests.*
import kotlinx.android.synthetic.main.activity_store_locater.*

class MyMessaging : AppCompatActivity() {

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
        setContentView(R.layout.activity_my_messages)

        navigation_interests.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_interests)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        bottomNavigation.getMenu().findItem(R.id.navigation_user_market).setChecked(true)

        val send_message = findViewById<EditText>(R.id.send_message)
        val messages = findViewById<TextView>(R.id.messages)

        val send_message_button = findViewById<Button>(R.id.send_message_button)

        // set on-click listener
        send_message_button.setOnClickListener {
            Toast.makeText(this, "Send Message! " + send_message.text, Toast.LENGTH_LONG).show()
            messages.text =  send_message.text
        }
    }
}
