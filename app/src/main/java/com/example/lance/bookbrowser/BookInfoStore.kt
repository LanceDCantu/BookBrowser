package com.example.lance.bookbrowser

import android.content.Intent
import android.os.Bundle
import com.example.lance.bookbrowser.UserData
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.example.lance.bookbrowser.Cart.Cart
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.android.synthetic.main.book_info_store.*
import android.text.Html
import com.bumptech.glide.Glide
import com.example.lance.bookbrowser.StoreLocater.StoreLocater


class BookInfoStore : AppCompatActivity() {

    private lateinit var isbn: String
    private lateinit var functions: FirebaseFunctions

    private lateinit var book_info: Book

    private var book_in_cart: Boolean = false

    val users_ref = FirebaseDatabase.getInstance("https://bookbrowser-9108e-users.firebaseio.com").reference
    val product_ref = FirebaseDatabase.getInstance("https://bookbrowser-9108e.firebaseio.com").reference
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
        setContentView(R.layout.book_info_store)

        functions = FirebaseFunctions.getInstance()

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
                    Toast.makeText(this@BookInfoStore, e?.message, Toast.LENGTH_LONG).show()
                    return@OnCompleteListener
                    // [END_EXCLUDE]
                }

                // [START_EXCLUDE]

                val url = task.getResult()?.get("bookImageURL") as String
                val imageView = findViewById<ImageView>(R.id.imageView2)
                Glide.with(this@BookInfoStore).load(url).into(imageView)

                val description = task.getResult()?.get("description") as String
                val descriptionView = findViewById<TextView>(R.id.book_description_text)
                descriptionView.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT))
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

        checkCartStatus(isbn)

        // set on-click listener
        add_to_cart_button.setOnClickListener {
            val myUser = UserData.getData()
            //println(myUser)
            var sending_book = Book("none", "none", "none", 0.0, "none")

            sending_book.title = book_info.title
            sending_book.author = book_info.author
            sending_book.price = book_info.price
            sending_book.store = spinner.getSelectedItem().toString()


            if(add_to_cart_button.text == "Remove from Cart")
            {
                val pushRef: DatabaseReference = users_ref.child(("$myUser") + "/" + "cart/" + book_info.isbn + "/")

                pushRef.setValue(null)

                Toast.makeText(this@BookInfoStore, "Removed from Cart", Toast.LENGTH_LONG).show()
                //remove the book from the cart
                add_to_cart_button.text = "Add to Cart"
            }
            else if (spinner.getSelectedItem().toString() == "Select Store") {
                Toast.makeText(this@BookInfoStore, "Please Select a Store", Toast.LENGTH_SHORT).show()
            }
            else if(add_to_cart_button.text == "Add to Cart")
            {
                val pushRef: DatabaseReference =
                    users_ref.child(("$myUser") + "/" + "cart/" + book_info.isbn + "/")

                pushRef.setValue(sending_book)

                add_to_cart_button.text = "Remove from Cart"

                Toast.makeText(this@BookInfoStore, "Added to Cart", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initBookData(isbn: String) {
        book_info = Book("none", "none", "none", 0.0, "none")

        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val title_textView: TextView = findViewById(R.id.book_title_text)
                val author_textView: TextView = findViewById(R.id.book_author_text)
                val price_textView: TextView = findViewById(R.id.book_price_text)
                val isbn_textView: TextView = findViewById(R.id.book_isbn_text)
                val description_textView: TextView = findViewById(R.id.book_description_text)

                title_textView.text = dataSnapshot.child("/title/").value.toString()
                author_textView.text = dataSnapshot.child("/authors/").children.elementAt(0).value.toString()
                price_textView.text = "$2.14"
                isbn_textView.text = "ISBN: " + dataSnapshot.key

                if (dataSnapshot.child("/longDescription/").value.toString() != "null") {
                    description_textView.text =
                        "Description: " + dataSnapshot.child("/longDescription/").value.toString()
                }

                book_info.price = 2.14
                book_info.title = dataSnapshot.child("/title/").value.toString()
                book_info.author = dataSnapshot.child("/authors/").children.elementAt(0).value.toString()
                book_info.isbn = dataSnapshot.key.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }

        }
        product_ref.child("products/" + isbn + "/").addListenerForSingleValueEvent(userListener)
    }

    private fun checkCartStatus(isbn: String){
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (cart_item_snap in dataSnapshot.children)
                {
                    book_in_cart = false

                    if(cart_item_snap.key == isbn)
                    {
                        book_in_cart = true
                        val add_to_cart_button = findViewById<Button>(R.id.add_to_cart)
                        add_to_cart_button.text = "Remove from Cart"
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }

        }
        users_ref.child(myUser + "/cart/").addListenerForSingleValueEvent(userListener)
}

    private fun initializeBottomNavigation() {
        navigation_book_store.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_book_store)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        bottomNavigation.getMenu().findItem(R.id.navigation_browse).setChecked(true)
    }

    private fun fetchCloudBookInfo(book_isbn: String): Task<Map<String, Any>> {
        val data = hashMapOf(
            "isbn" to book_isbn
        )

        return functions
            .getHttpsCallable("bookInfov2")
            .call(data)
            .continueWith{ task ->
                val result = task.result?.data as Map<String, Any>
                result

            }
    }
}

