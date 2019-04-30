package com.example.lance.bookbrowser

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.lance.bookbrowser.Cart.Cart
import com.example.lance.bookbrowser.OrderHistory.OrderHistory
import com.example.lance.bookbrowser.StoreLocater.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.my_account.*

class MyAccount : AppCompatActivity() {

    val secondary = FirebaseDatabase.getInstance("https://bookbrowser-9108e-users.firebaseio.com").reference
    var storageRef = FirebaseStorage.getInstance().reference.child("profile_pics/" + "blue.jpg")

    var user : User = User ("none", "null", "null","null")

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
        setContentView(R.layout.my_account)

        navigation_account.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_account)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        bottomNavigation.getMenu().findItem(R.id.navigation_account).setChecked(true)

        val order_history_button = findViewById<Button>(R.id.order_history_button)

        order_history_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                startActivity(Intent(this@MyAccount, OrderHistory::class.java))
            }
        })

        initUserData()

        val change_picture = findViewById<TextView>(R.id.change_picture)

        //Wait for the user to change their picture
        change_picture.setOnClickListener {
            //go to the add a picture situation
            Toast.makeText(this@MyAccount, "Picture Addition", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initUserData()
    {
        val menuListener = object : ValueEventListener
        {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                //write method to extract data in a User object
                user.picture = dataSnapshot.child("picture/").value.toString()
                user.successful_sales = dataSnapshot.child("successful_sales/").value.toString()
                user.thumbs_up = dataSnapshot.child("thumbs_up/").value.toString()
                user.thumbs_down = dataSnapshot.child("thumbs_down/").value.toString()

                val email_TextView: TextView = findViewById(R.id.email_text_input)
                email_TextView.text = dataSnapshot.key!!.replace('!','.',true)

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
                else
                {
                    val change_picture = findViewById<TextView>(R.id.change_picture)
                    change_picture.text = "Add"
                }
            }

            override fun onCancelled(databaseError: DatabaseError)
            {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        //this is how we query for the specific user, we need to make the "lancedcantu" dynamic
        //val profile = intent.getStringExtra("user_id")
        //println(profile)
        val myUser = UserData.getData()
        //println(myUser)
        secondary.child(myUser + "/").addListenerForSingleValueEvent(menuListener)
    }
}


