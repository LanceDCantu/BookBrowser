package com.example.lance.bookbrowser.MyOffers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.lance.bookbrowser.R

class AddOffer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_offer)

        val add_offers_button = findViewById<Button>(R.id.add_offer_button)

        // set on-click listener
        add_offers_button.setOnClickListener {
            AddMarketOffer()
        }
    }

    private fun AddMarketOffer ()
    {
        //Add the market offer to the user account via the firebase database.
        //This means we will need to figure out Algolia "integration"
    }

}
