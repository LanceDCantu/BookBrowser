package com.example.lance.bookbrowser

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.example.lance.bookbrowser.Cart.Cart
import com.example.lance.bookbrowser.MyOffers.AddOffer
import com.google.firebase.database.*
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.android.synthetic.main.activity_store_locater.*
import kotlinx.android.synthetic.main.book_info_market.*

class BookInfoMarket : AppCompatActivity() {

    private lateinit var isbn : String
    private lateinit var market_id : String
    private var is_owner : Boolean = false

    private lateinit var functions: FirebaseFunctions

    private lateinit var book_info : Book

    private var is_interested : Boolean = false

    val market_ref = FirebaseDatabase.getInstance("https://bookbrowser-9108e-market.firebaseio.com/").reference
    val users_ref = FirebaseDatabase.getInstance("https://bookbrowser-9108e-users.firebaseio.com/").reference
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

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_info_market)

        val intent = intent
        isbn = intent.getStringExtra("book_isbn")
        market_id = intent.getStringExtra("market_id")
        is_owner = intent.getBooleanExtra("is_owner", false)

        val data = hashMapOf(
            "isbn" to isbn,
            "push" to true
        )

        functions = FirebaseFunctions.getInstance()

        functions
            .getHttpsCallable("bookInfo")
            .call(data)
            .addOnFailureListener {
                Toast.makeText(this, "we didn't do it!", Toast.LENGTH_SHORT).show()
            }
            .addOnSuccessListener {
                Toast.makeText(this, "we did it!", Toast.LENGTH_SHORT).show()
            }

        initBookData()

        navigation_book_market.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_book_market)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        var interests_add = findViewById<Button>(R.id.add_to_interests_button)
        var contact_buttons = findViewById<Button>(R.id.contact_button)

        if(is_owner)
        {
            interests_add.visibility = View.GONE
            contact_buttons.text = "View Messages"
        }

        //this checks the initial button state and the rest comes from toggling
        //the only issue that can occur here is fast pressing causing issues with removing and adding
        checkInterestStatus(market_id)

        // set on-click listener
        interests_add.setOnClickListener {
            if(is_interested)
            {
                //remove from interests
                interests_add.text = "Add to My Interests"
                is_interested = false

                removeFromInterests(market_id)
            }
            else
            {
                //add to interests
                interests_add.text = "Remove from My Interests"
                is_interested = true

                addToInterests(market_id)
            }
        }
    }

    private fun initBookData()
    {
        val menuListener = object : ValueEventListener
        {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                val title_textView: TextView = findViewById(R.id.book_title_text)
                val author_textView: TextView = findViewById(R.id.book_author_text)
                val price_textView: TextView = findViewById(R.id.price_text)
                val seller_textView: TextView = findViewById(R.id.seller_name_text)

                title_textView.text = dataSnapshot.child("/title/").value.toString()
                author_textView.text = dataSnapshot.child("/authors/").value.toString()
                price_textView.text = dataSnapshot.child("/asking_price/").value.toString()
                seller_textView.text = dataSnapshot.child("/seller/").value.toString()
            }

            override fun onCancelled(databaseError: DatabaseError)
            {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        market_ref.child(market_id + "/").addListenerForSingleValueEvent(menuListener)
    }

    private fun checkInterestStatus(marketId : String)
    {
        var interests_add = findViewById<Button>(R.id.add_to_interests_button)

        val userListener = object : ValueEventListener
        {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                //read in a datasnapshot and enable button depending on
                for (market_offer in dataSnapshot.children)
                {
                    if(market_offer.value.toString() == marketId)
                    {
                        interests_add.text = "Remove from My Interests"
                        is_interested = true

                        return
                    }
                }

                is_interested = false
            }

            override fun onCancelled(databaseError: DatabaseError)
            {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }

        }
        users_ref.child(myUser + "/" + "my_interests/").addListenerForSingleValueEvent(userListener)
    }

    private fun addToInterests(market_id_add : String)
    {
        println(myUser)
        var pushRef: DatabaseReference = users_ref.child(myUser + "/" + "my_interests/" + market_id_add)

        pushRef.setValue(market_id_add)

        Toast.makeText(this@BookInfoMarket, "Added to Interests", Toast.LENGTH_LONG).show()
    }

    private fun removeFromInterests(market_id_remove : String)
    {
        val userListener = object : ValueEventListener
        {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                dataSnapshot.child("/" + market_id_remove).getRef().removeValue();

                Toast.makeText(this@BookInfoMarket, "Added to Interests", Toast.LENGTH_LONG).show()
            }

            override fun onCancelled(databaseError: DatabaseError)
            {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }

        }
        users_ref.child(myUser + "/" + "my_interests/").addListenerForSingleValueEvent(userListener)
    }
}
