package com.example.lance.bookbrowser

import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
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
        var task_result = fetchCloudBookInfo(isbn)

        var test = task_result.result

        navigation_book_store.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_book_store)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        initBookData()

        initializeBottomNavigation()

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

    private fun initBookData()
    {
        book_info = Book("none", "none", "none", 0.0, "none")

        book_info.store = "Grassroots Books"
        book_info.price = 7.23
        book_info.title = "Crazy Rich Asians"
        book_info.author = "Kevin Kwan"
        //book_info.isbn = isbn //This gets the isbn properly from what we send in

        book_info.isbn = "9786020618753"
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
