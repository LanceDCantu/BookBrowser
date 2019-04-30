package com.example.lance.bookbrowser.Cart

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.lance.bookbrowser.*
import com.example.lance.bookbrowser.R
import com.example.lance.bookbrowser.StoreLocater.StoreLocater
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_cart.*
import java.text.SimpleDateFormat
import com.example.lance.bookbrowser.UserData


class Cart :  AppCompatActivity(), CartFragment.OnCartEntryListener {

    val users_ref = FirebaseDatabase.getInstance("https://bookbrowser-9108e-users.firebaseio.com").reference
    val orders_ref = FirebaseDatabase.getInstance("https://bookbrowser-9108e-orders.firebaseio.com").reference
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

    override fun SetMonetaryValuesCart(monetaryValues : DoubleArray) {
        var subtotalText: TextView = findViewById(R.id.subtotal_text)
        var feesText: TextView = findViewById(R.id.fees_text)
        var taxText: TextView = findViewById(R.id.tax_text)
        var totalText: TextView = findViewById(R.id.total_text)

        subtotalText.text =  "$" + String.format("%.2f", monetaryValues[0])
        feesText.text = "$" + String.format("%.2f", monetaryValues[1])
        taxText.text = "$" + String.format("%.2f", monetaryValues[2])
        totalText.text = "$" + String.format("%.2f", monetaryValues[3])
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        navigation_bar_cart.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_bar_cart)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        bottomNavigation.getMenu().findItem(R.id.navigation_cart).setChecked(true)

        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            val fragment = CartFragment()
            transaction.replace(R.id.sample_content_fragment, fragment)
            transaction.commit()
        }

        // get reference to button
        val reserve_books = findViewById<Button>(R.id.reserve_book_button)

        // set on-click listener
        reserve_books.setOnClickListener {
            val menuListener = object : ValueEventListener
            {
                @SuppressLint("NewApi", "SimpleDateFormat")
                override fun onDataChange(dataSnapshot: DataSnapshot)
                {
                    val username = myUser?.substringBefore("@")
                    //println("$username")
                    var sending_order =
                        Order("none", "none", 0.0, "none", "$username", mutableListOf())
                    var temp_book = Book("none", "none", "none", 0.0, "none")

                    val date = Calendar.getInstance().time
                    val dateFormatter = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                    val dateTimeString = dateFormatter.format(date)
                    sending_order.date_placed = dateTimeString.split(' ')[0]
                    sending_order.time_placed = dateTimeString.split(' ')[1]

                    var index = 0

                    for (cart_item_snap in dataSnapshot.children)
                    {
                        temp_book.title = cart_item_snap.child("/title/").value.toString()
                        temp_book.author = cart_item_snap.child("/author/").value.toString()
                        temp_book.price = cart_item_snap.child("/price/").value as Double
                        temp_book.store = cart_item_snap.child("/store/").value.toString()

                        sending_order.books.add(temp_book)

                        sending_order.store = temp_book.store//Note this assumes all the books are from the same place
                        sending_order.total += temp_book.price

                        index++
                    }

                    if(sending_order.books.size != 0) {

                        var pushRef: DatabaseReference = orders_ref.push()

                        var cartPushRef: DatabaseReference = users_ref.child(myUser + "/" + "cart/")

                        cartPushRef.setValue(null)

                        pushRef.setValue(sending_order)

                        Toast.makeText(this@Cart, "Books Reserved!", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onCancelled(databaseError: DatabaseError)
                {
                    println("loadPost:onCancelled ${databaseError.toException()}")
                }
            }
            //this is how we query for the specific user, we need to make the "lancedcantu" dynamic
            users_ref.child(myUser + "/" + "cart/").addListenerForSingleValueEvent(menuListener)
        }
    }
}






