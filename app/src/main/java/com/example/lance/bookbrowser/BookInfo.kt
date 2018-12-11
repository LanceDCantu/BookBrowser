package com.example.lance.bookbrowser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.constraint.Group
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity

import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import com.example.lance.bookbrowser.notification.NotificationCountSetClass
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

import com.google.firebase.database.*
import com.google.firebase.database.core.Constants
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.book_info.*

class BookInfo : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.checkReviews->{
                val intent = Intent(this, reviews::class.java)
                //intent.putExtra("keyIdentifier", value)
                startActivity(intent)
            }
            //this code will help with updating the cart
/*
            R.id.addButton->{
                cartItems++
                NotificationCountSetClass.setNotifyCount(cartItems)
                invalidateOptionsMenu() //refresh counter
            }

            R.id.button3->{
                cartItems--
                NotificationCountSetClass.setNotifyCount(cartItems)
                invalidateOptionsMenu() //refresh counter
            }*/

        }
    }

    var cartItems = 0
    override fun onCreate(savedInstanceState: Bundle?){

        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_info)

        addButton.setOnClickListener(this)
        checkReviews.setOnClickListener(this)

    }
    //this code will help with updating the cart
/*
    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        var item = menu.findItem(R.id.action_cart)
        NotificationCountSetClass.setAddToCart(this@BookInfo, item, cartItems!!)
        return super.onPrepareOptionsMenu(menu)
    }*/
}
