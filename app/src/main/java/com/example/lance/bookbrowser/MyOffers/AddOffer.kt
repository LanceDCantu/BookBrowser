package com.example.lance.bookbrowser.MyOffers

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.lance.bookbrowser.*
import com.example.lance.bookbrowser.Cart.Cart
import com.example.lance.bookbrowser.StoreLocater.StoreLocater
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.android.synthetic.main.activity_add_offer.*
import kotlinx.android.synthetic.main.activity_market_search.*
import com.example.lance.bookbrowser.UserData
import android.widget.EditText
import com.algolia.search.saas.Client
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONObject

class AddOffer : AppCompatActivity() {

    private lateinit var functions: FirebaseFunctions

    val market_ref = FirebaseDatabase.getInstance("https://bookbrowser-9108e-market.firebaseio.com").reference
    val users_ref = FirebaseDatabase.getInstance("https://bookbrowser-9108e-users.firebaseio.com/").reference

    var client = Client("BFI0J5Z2YU", "e934ce140ba7da63e5aa64d77c563eec")
    var index = client.getIndex("market")

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
        setContentView(R.layout.activity_add_offer)

        functions = FirebaseFunctions.getInstance()

        navigation_add_offer.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_add_offer)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        bottomNavigation.getMenu().findItem(R.id.navigation_user_market).setChecked(true)

        val add_offers_button = findViewById<Button>(R.id.add_offer_button)

        // set on-click listener
        add_offers_button.setOnClickListener {
            //send in isbn
            AddMarketOffer()
        }
    }

    private fun AddMarketOffer ()
    {
        //Add the market offer to the user account via the firebase database.
        //This means we will need to figure out Algolia "integration"
        //

        val isbn_edt = findViewById<EditText>(R.id.isbn_edt)
        val price_edt = findViewById<EditText>(R.id.asking_edt)
        val notes_edt = findViewById<EditText>(R.id.notes_edt)

        var isbn: String = isbn_edt.text.toString()
        var price: String = price_edt.text.toString()
        var notes: String = notes_edt.text.toString()
        var title: String = ""
        var author: String = ""
        var authors: String = ""
        var seller: String? = myUser?.substringBefore("@", "error")

        fetchCloudBookInfo(isbn)
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    val e = task.exception
                    Toast.makeText(this@AddOffer, e?.message, Toast.LENGTH_LONG).show()
                    return@OnCompleteListener
                    // [END_EXCLUDE]
                }

                //get title and author store it
                title = task.result?.get("title") as String

                var author_hash: HashMap <String,String> = task.result?.get("author") as HashMap<String, String>

                var author_list: ArrayList<String> = author_hash["author"] as ArrayList<String>

                var author_hash2: HashMap <String,String> = author_list[0] as HashMap<String, String>

                author = author_hash2["name"].toString()

                var sending_temp = Temp("none", "none", "none", "none", "none", "none")

                sending_temp.isbn = isbn
                sending_temp.asking_price = price
                sending_temp.notes = notes
                sending_temp.title = title
                sending_temp.authors = author
                if(seller != null) {
                    sending_temp.seller = seller
                }

                var pushRef: DatabaseReference = market_ref.push()

                pushRef.setValue(sending_temp)

                val temp_object = JSONObject()
                    .put("isbn", isbn)
                    .put("asking_price", "$" + price)
                    .put("notes", notes)
                    .put("title",  title)
                    .put("authors",  author)
                    .put("seller", seller)
                    .put("market_id", pushRef.key)

                index.addObjectAsync(temp_object, null)

                var userPushRef: DatabaseReference = users_ref.child(myUser + "/" + "my_offers/" + pushRef.key)

                userPushRef.setValue(pushRef.key)

                val intent = Intent(this, MyOffers::class.java)
                startActivity(intent)

                // [END_EXCLUDE]
            })
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
