package com.example.lance.bookbrowser

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.*
import java.text.SimpleDateFormat

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class Cart :  AppCompatActivity() {

    val users_ref = FirebaseDatabase.getInstance("https://bookbrowser-9108e-users.firebaseio.com").reference
    val orders_ref = FirebaseDatabase.getInstance("https://bookbrowser-9108e-orders.firebaseio.com").reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            val fragment = CartRecyclerViewFragment()
            transaction.replace(R.id.sample_content_fragment, fragment)
            transaction.commit()
        }

        // get reference to button
        val reserve_books = findViewById<Button>(R.id.reserve_book_button)

        // set on-click listener
        reserve_books.setOnClickListener {
            Toast.makeText(this, "You clicked me.", Toast.LENGTH_SHORT).show()

            val menuListener = object : ValueEventListener
            {
                @SuppressLint("NewApi", "SimpleDateFormat")
                override fun onDataChange(dataSnapshot: DataSnapshot)
                {
                    var sending_order = Order("none", "none", 0.0, "none", "lancedcantu", mutableListOf())
                    var temp_book = Book(1, "none", "none",0.0, "none")

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
                        temp_book.cost = cart_item_snap.child("/price/").value as Double
                        temp_book.store = cart_item_snap.child("/store/").value.toString()

                        sending_order.books.add(temp_book)

                        sending_order.store = temp_book.store//Note this assumes all the books are from the same place
                        sending_order.total += temp_book.cost

                        index++
                    }

                    var pushRef : DatabaseReference = orders_ref.push()
                    var pushKey : String = pushRef.key!!

                    pushRef.setValue(sending_order)

                    Toast.makeText(this@Cart, "Check it.", Toast.LENGTH_SHORT).show()
                }
                override fun onCancelled(databaseError: DatabaseError)
                {
                    println("loadPost:onCancelled ${databaseError.toException()}")
                }
            }
            //this is how we query for the specific user, we need to make the "lancedcantu" dynamic
            users_ref.child("users/" + "lancedcantu/" + "cart/").addListenerForSingleValueEvent(menuListener)
        }
    }
}






