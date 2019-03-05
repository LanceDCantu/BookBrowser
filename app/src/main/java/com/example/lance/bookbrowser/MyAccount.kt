package com.example.lance.bookbrowser

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

import kotlinx.android.synthetic.main.activity_main_bn.*

class MyAccount : AppCompatActivity() {

    val secondary = FirebaseDatabase.getInstance("https://bookbrowser-9108e-users.firebaseio.com").reference
    var storageRef = FirebaseStorage.getInstance().reference.child("profile_pics/" + "blue.jpg")

    var user : User = User (MutableList(1) { Book(1, "none", "none",0.0, "none") }, "none", "none", 0,0,0)

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_stores -> {
                val intent = Intent(this, StoreLocater::class.java)
                startActivity(intent)
                //message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_user_market -> {
                //message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_browse -> {
                //val recyclerViewFragment = RecyclerViewFragment(Array(60) { Book(1, "none", "none", 0.0, "none") })
                //openFragment(recyclerViewFragment)

                //val intent = Intent(this, StoreLocater::class.java)
                //startActivity(intent)
                //message.setText(R.string.title_notifications)
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
        setContentView(R.layout.my_account_fragment)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        initUserData()
    }

    private fun initUserData()
    {
        val menuListener = object : ValueEventListener
        {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                //write method to extract data in a User object
                user.email = dataSnapshot.child("/user_info/email/").value.toString()
                user.picture = dataSnapshot.child("/user_info/picture/").value.toString()
                user.successful_sales = dataSnapshot.child("/user_info/successful sales/").value as Long
                user.thumbs_up = dataSnapshot.child("/user_info/thumbs up/").value as Long
                user.thumbs_down = dataSnapshot.child("/user_info/thumbs down/").value as Long

                val username_textView: TextView = findViewById(R.id.username_text_input)
                username_textView.text = dataSnapshot.key

                val email_TextView: TextView = findViewById(R.id.email_text_input)
                email_TextView.text = user.email

                val successful_sales_TextView: TextView = findViewById(R.id.successful_sales_text)
                successful_sales_TextView.text = user.successful_sales.toString()

                val thumbsup_TextView: TextView = findViewById(R.id.thumbs_up_text)
                thumbsup_TextView.text = user.thumbs_up.toString()

                val thumbsdown_TextView: TextView = findViewById(R.id.thumbs_down_text)
                thumbsdown_TextView.text = user.thumbs_down.toString()

                //var profile_pic_view : ImageView = findViewById(R.id.profile_pic_view)
                //GlideApp.with(this@MyAccount).load(storageRef).into(profile_pic_view)
            }

            override fun onCancelled(databaseError: DatabaseError)
            {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        //this is how we query for the specific user, we need to make the "lancedcantu" dynamic
        secondary.child("users/" + "lancedcantu/").addListenerForSingleValueEvent(menuListener)
    }
}

