package com.example.lance.bookbrowser

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner

class BookInfo : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){


        }
    }

    var cartItems = 0
    override fun onCreate(savedInstanceState: Bundle?){

        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_info)

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
    }
/*
    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        var item = menu.findItem(R.id.action_cart)
        NotificationCountSetClass.setAddToCart(this@BookInfo, item, cartItems!!)
        return super.onPrepareOptionsMenu(menu)
    }*/
}
