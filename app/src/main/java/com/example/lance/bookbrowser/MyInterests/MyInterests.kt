package com.example.lance.bookbrowser.MyInterests

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.lance.bookbrowser.R

class MyInterests : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_interests)

        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            val fragment = MyInterestsFragment()
            transaction.replace(R.id.sample_content_fragment, fragment)
            transaction.commit()
        }
    }
}
