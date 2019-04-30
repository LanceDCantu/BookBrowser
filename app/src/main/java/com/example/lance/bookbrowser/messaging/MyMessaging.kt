package com.example.lance.bookbrowser.messaging

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.lance.bookbrowser.*
import com.example.lance.bookbrowser.Cart.Cart
import com.example.lance.bookbrowser.R
import com.example.lance.bookbrowser.StoreLocater.StoreLocater
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_my_interests.*

class MyMessaging : AppCompatActivity() {

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
        setContentView(R.layout.emily_messages_activity)

        navigation_interests.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)



        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_interests)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        bottomNavigation.getMenu().findItem(R.id.navigation_user_market).setChecked(true)

        val send_message = findViewById<EditText>(R.id.send_message)
        val messages = findViewById<TextView>(R.id.messages)

        val send_message_button = findViewById<Button>(R.id.send_message_button)

        // reference to the messages database
        val messages_ref = FirebaseDatabase.getInstance("https://bookbrowser-9108e-messages-1dd33.firebaseio.com/").reference

        val childListener = object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
                //Empty
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                //Empty
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                //Empty
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                //Empty
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {

                val message = dataSnapshot.getValue<Message>(Message::class.java)

              //  messagesList.plus(message)

                var current = messages.text.toString()
                current += '\n'
                current += message?.data
                messages.text = current

            }
        }
      /*  val messageListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

               dataSnapshot.children.mapNotNullTo(messagesList){
                   it.getValue<Message>(Message::class.java)
               }
                messagesList.forEach{
                    println("message: " + it?.data)
                }



            }



            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }

*/

   //     messages_ref.addListenerForSingleValueEvent(messageListener)
        messages_ref.addChildEventListener(childListener)


        // set on-click listener
        send_message_button.setOnClickListener {
            val sendText = send_message.text.toString()
            if(!sendText.isEmpty()) {
                Toast.makeText(this, "Send Message! " + sendText, Toast.LENGTH_LONG).show()

                var pushRef: DatabaseReference = messages_ref.push()

                var message = Message(sendText)


                pushRef.setValue(message)

                Toast.makeText(this, "Added to Messages", Toast.LENGTH_LONG).show()

                send_message.setText("")
            }
        }
    }

   // private val messagesList : MutableList<Message> = mutableListOf()

    companion object {

        private val TAG = "MyMessaging"

    }
}
