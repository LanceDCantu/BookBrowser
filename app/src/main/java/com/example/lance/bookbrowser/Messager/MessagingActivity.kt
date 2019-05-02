package com.example.lance.bookbrowser.Messager

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.KeyEvent
import com.example.lance.bookbrowser.Cart.Cart
import com.example.lance.bookbrowser.StoreLocater.StoreLocater
import kotlinx.android.synthetic.main.activity_messaging.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.EditText
import android.widget.TextView.OnEditorActionListener
import com.example.lance.bookbrowser.*
import com.example.lance.bookbrowser.R
import com.google.firebase.database.*


class MessagingActivity : AppCompatActivity() {

    lateinit var market_id: String
    lateinit var buyer: String

    val myUser = UserData.getData()

    val market_ref = FirebaseDatabase.getInstance("https://bookbrowser-9108e-market.firebaseio.com").reference

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
        setContentView(R.layout.activity_messaging)

        market_id = intent.getStringExtra("id")
        buyer = intent.getStringExtra("buyer")

        if(buyer != "")
        {
            //then we want to send in the buyer so we do nothing
        }
        else
        {
            //then we are the buyer
            buyer = myUser.toString().substringBefore("@")
        }

        message_activity_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.message_activity_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        bottomNavigation.getMenu().findItem(R.id.navigation_user_market).setChecked(true)

        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            val fragment = MessageFragment()
            val args = Bundle()
            args.putString("id", market_id)
            args.putString("buyer", buyer)
            fragment.arguments = args
            transaction.replace(R.id.sample_content_fragment, fragment)
            transaction.commit()
        }

        // your text box
        val message = findViewById(R.id.message_edit) as EditText

        message.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    sendMessage()
                    true
                }
                else -> false
            }
        }

        //we need to totally revamp this as a recycler view
    }

    fun sendMessage()
    {
        val message = findViewById(R.id.message_edit) as EditText

        var sending_message = Message("none", false)

        if(message.text != null)
        {
            sending_message.data = message.text.toString()
        }

        //then we are the buyer and so the message should be on the right and so we send it with true
        sending_message.sender = buyer == myUser.toString().substringBefore("@")

        var pushRef: DatabaseReference = market_ref.child(market_id + "/conversations/" + buyer + "/").push()
        pushRef.setValue(sending_message)

        message.text.clear()
    }

}
