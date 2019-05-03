package com.example.lance.bookbrowser

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.lance.bookbrowser.Cart.Cart
import com.example.lance.bookbrowser.OrderHistory.OrderHistory
import com.example.lance.bookbrowser.StoreLocater.*
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.my_account.*
import kotlinx.android.synthetic.main.user_account.*

class UserAccount : AppCompatActivity() {

    val secondary = FirebaseDatabase.getInstance("https://bookbrowser-9108e-users.firebaseio.com").reference
    var storageRef = FirebaseStorage.getInstance().reference.child("profile_pics/" + "blue.jpg")

    var user : User = User ("none", "0", "0","0")

    var username: String = ""
    var username_full: String? = ""

    var upvote: Boolean = false
    var downvote: Boolean = false

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
        setContentView(R.layout.user_account)

        navigation_user_account.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_user_account)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        bottomNavigation.getMenu().findItem(R.id.navigation_user_market).setChecked(true)

        username = intent.getStringExtra("account_name")

        initUserData()

        var thumbs_up_button = findViewById<ImageView>(R.id.thumbs_up)
        var thumbs_down_button = findViewById<ImageView>(R.id.thumbs_down)

        thumbs_up_button.setOnClickListener {
            upvote = true
            thumbs_up_button.setImageResource(R.drawable.baseline_thumb_up_24)

            val thumbs_up_text_view = findViewById<TextView>(R.id.thumbs_up_text)
            val thumbs_down_text_view = findViewById<TextView>(R.id.thumbs_down_text)

            var thumbs_up_long : Long = thumbs_up_text_view.text.toString().toLong()
            var thumbs_down_long : Long = thumbs_down_text_view.text.toString().toLong()

            thumbs_up_long++

            if(downvote)
            {
                //change to unfilled and subtract an upvote
                thumbs_down_button.setImageResource(R.drawable.outline_thumb_down_24)
                downvote = false

                thumbs_down_long--
            }

            thumbs_up_text_view.text = thumbs_up_long.toString()
            thumbs_down_text_view.text = thumbs_down_long.toString()

            var pushRefUp: DatabaseReference = secondary.child(username_full + "/thumbs_up/")
            pushRefUp.setValue(thumbs_up_long.toString())
            var pushRefDown: DatabaseReference = secondary.child(username_full + "/thumbs_down/")
            pushRefDown.setValue(thumbs_down_long.toString())
        }

        thumbs_down_button.setOnClickListener {
            downvote = true
            thumbs_down_button.setImageResource(R.drawable.baseline_thumb_down_24)

            val thumbs_up_text_view = findViewById<TextView>(R.id.thumbs_up_text)
            val thumbs_down_text_view = findViewById<TextView>(R.id.thumbs_down_text)

            var thumbs_up_long : Long = thumbs_up_text_view.text.toString().toLong()
            var thumbs_down_long : Long = thumbs_down_text_view.text.toString().toLong()

            thumbs_down_long++

            if(upvote)
            {
                //change to unfilled and subtract an upvote
                upvote = false
                thumbs_up_button.setImageResource(R.drawable.outline_thumb_up_24)

                thumbs_up_long--
            }

            thumbs_up_text_view.text = thumbs_up_long.toString()
            thumbs_down_text_view.text = thumbs_down_long.toString()

            var pushRefUp: DatabaseReference = secondary.child(username_full + "/thumbs_up/")
            pushRefUp.setValue(thumbs_up_long.toString())
            var pushRefDown: DatabaseReference = secondary.child(username_full + "/thumbs_down/")
            pushRefDown.setValue(thumbs_down_long.toString())
        }
    }

    private fun initUserData()
    {
        val menuListener = object : ValueEventListener
        {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                for (user in dataSnapshot.children)
                {
                    if(user.key.toString().contains(username))
                    {
                        username_full = user.key
                        break
                    }
                }

                //write method to extract data in a User object
                user.picture = dataSnapshot.child(username_full + "/picture/").value.toString()
                user.successful_sales = dataSnapshot.child(username_full + "/successful_sales/").value.toString()
                user.thumbs_up = dataSnapshot.child(username_full + "/thumbs_up/").value.toString()
                user.thumbs_down = dataSnapshot.child(username_full + "/thumbs_down/").value.toString()

                val username_TextView: TextView = findViewById(R.id.username_text_input)
                username_TextView.text = username

                val successful_sales_TextView: TextView = findViewById(R.id.successful_sales_text)
                successful_sales_TextView.text = user.successful_sales.toString()

                val thumbsup_TextView: TextView = findViewById(R.id.thumbs_up_text)
                thumbsup_TextView.text = user.thumbs_up.toString()

                val thumbsdown_TextView: TextView = findViewById(R.id.thumbs_down_text)
                thumbsdown_TextView.text = user.thumbs_down.toString()

                //If a picture exists for the user
                if(user.picture != "")
                {
                    //Retrieve the picture from the Firestore via the email as the file name
                    //var profile_pic_view : ImageView = findViewById(R.id.profile_pic_view)
                    //GlideApp.with(this@MyAccount).load(storageRef).into(profile_pic_view)
                }
            }

            override fun onCancelled(databaseError: DatabaseError)
            {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        secondary.addListenerForSingleValueEvent(menuListener)
    }
}


