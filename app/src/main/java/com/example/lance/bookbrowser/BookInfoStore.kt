package com.example.lance.bookbrowser

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.lance.bookbrowser.Cart.Cart
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.android.synthetic.main.activity_store_locater.*
import kotlinx.android.synthetic.main.book_info_store.*

class BookInfoStore : AppCompatActivity() {

    private lateinit var isbn : String
    private lateinit var functions: FirebaseFunctions

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

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_info_store)

        val intent = intent
        isbn = intent.getStringExtra("book_isbn")

       val data = hashMapOf(
            "isbn" to isbn
           // "push" to true
        )


        functions = FirebaseFunctions.getInstance()

        functions
            .getHttpsCallable("bookInfo")
            .call(data)
            .addOnFailureListener {
                Toast.makeText(this, it.message,Toast.LENGTH_LONG).show()
            }
            .addOnSuccessListener {
                Toast.makeText(this, "we did it!", Toast.LENGTH_SHORT).show()
            }

        navigation_book_store.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_book_store)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        bottomNavigation.getMenu().findItem(R.id.navigation_browse).setChecked(true)

        val spinner: Spinner = findViewById(R.id.stores_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.stores_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

    }
}
