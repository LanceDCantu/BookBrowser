package com.example.lance.bookbrowser.MyOffers

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.example.lance.bookbrowser.MyInterests.MyOffersFragment
import com.example.lance.bookbrowser.R
import com.example.lance.bookbrowser.StoreLocater
import kotlinx.android.synthetic.main.activity_my_offers.*


class MyOffers : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_offers)

        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            val fragment = MyOffersFragment()
            transaction.replace(R.id.sample_content_fragment, fragment)
            transaction.commit()
        }

        val add_button = findViewById<FloatingActionButton>(R.id.add_fab)

        // set on-click listener
        add_button.setOnClickListener {
            val intent = Intent(this, AddOffer::class.java)
            startActivity(intent)
        }
    }
}
