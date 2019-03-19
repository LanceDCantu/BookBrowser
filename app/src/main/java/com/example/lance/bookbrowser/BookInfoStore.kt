package com.example.lance.bookbrowser

import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.example.lance.bookbrowser.Cart.Cart
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.android.synthetic.main.activity_store_locater.*
import kotlinx.android.synthetic.main.book_info_store.*
import java.text.SimpleDateFormat

class BookInfoStore : AppCompatActivity() {

    private lateinit var isbn : String
    private lateinit var functions: FirebaseFunctions

    private lateinit var book_info : Book

    val users_ref = FirebaseDatabase.getInstance("https://bookbrowser-9108e-users.firebaseio.com").reference
    val product_ref = FirebaseDatabase.getInstance("https://bookbrowser-9108e.firebaseio.com").reference

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

        navigation_book_store.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_book_store)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        initBookData(isbn)

        initializeBottomNavigation()

        fetchCloudBookInfo(isbn)
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    val e = task.exception
                    Toast.makeText(this@BookInfoStore, "fail", Toast.LENGTH_LONG).show()
                    return@OnCompleteListener
                    // [END_EXCLUDE]
                }

                // [START_EXCLUDE]
                Toast.makeText(this@BookInfoStore, "pass", Toast.LENGTH_LONG).show()
                val result = task.result
                // [END_EXCLUDE]
            })

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

        // get reference to button
        val add_to_cart_button = findViewById<Button>(R.id.add_to_cart)

        // set on-click listener
        add_to_cart_button.setOnClickListener {
            var sending_book = Book("none", "none", "none", 0.0, "none")

            sending_book.title = book_info.title
            sending_book.author = book_info.author
            sending_book.price = book_info.price
            sending_book.store = spinner.getSelectedItem().toString()

            if(spinner.getSelectedItem().toString() == "Select Store")
            {
                Toast.makeText(this@BookInfoStore, "Please Select a Store", Toast.LENGTH_SHORT).show()
            }
            else
            {
                var pushRef: DatabaseReference = users_ref.child("lancedcantu@yahoo!com/" + "cart/" + book_info.isbn + "/")

                pushRef.setValue(sending_book)

                Toast.makeText(this@BookInfoStore, "Added to Cart", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initBookData(isbn : String)
    {
        book_info = Book("none", "none", "none", 0.0, "none")

        val userListener = object : ValueEventListener
        {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                val title_textView: TextView = findViewById(R.id.book_title_text)
                val author_textView: TextView = findViewById(R.id.book_author_text)
                val price_textView: TextView = findViewById(R.id.book_price_text)
                val isbn_textView: TextView = findViewById(R.id.book_isbn_text)
                val description_textView: TextView = findViewById(R.id.book_description_text)

                title_textView.text = dataSnapshot.child("/title/").value.toString()
                author_textView.text = dataSnapshot.child("/authors/").children.elementAt(0).value.toString()
                price_textView.text = "$2.14"
                isbn_textView.text = dataSnapshot.key

                if(dataSnapshot.child("/longDescription/").value.toString() != "null")
                {
                    description_textView.text =  "Description: " + dataSnapshot.child("/longDescription/").value.toString()
                }

                book_info.price = 2.14
                book_info.title = dataSnapshot.child("/title/").value.toString()
                book_info.author = dataSnapshot.child("/authors/").children.elementAt(0).value.toString()
                book_info.isbn = dataSnapshot.key.toString()
            }

            override fun onCancelled(databaseError: DatabaseError)
            {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }

        }
        product_ref.child("products/" + isbn + "/").addListenerForSingleValueEvent(userListener)
    }
    private fun initializeBottomNavigation()
    {
        navigation_book_store.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_book_store)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        bottomNavigation.getMenu().findItem(R.id.navigation_browse).setChecked(true)
    }

    private fun fetchCloudBookInfo(book_isbn : String) : Task <String> {
        val data = hashMapOf(
            "isbn" to book_isbn
        )

        functions = FirebaseFunctions.getInstance()

        return functions
            .getHttpsCallable("bookInfo")
            .call(data)
            .continueWith { task ->
                val result = task.result?.data as String
                result
            }
    }
}
