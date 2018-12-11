package com.example.lance.bookbrowser

import android.app.SearchManager
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.example.lance.bookbrowser.model.Database

class SearchableActivity : AppCompatActivity() {
    private val database = Database()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e("SearchableActivity", "onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchable)

        val listView = findViewById<ListView>(R.id.listView)
        Log.e("SearchableActivity", "listView=" + listView.toString())

        listView.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            public override fun onItemClick(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                val intent = Intent(baseContext, BookInfo::class.java)
                //intent.putExtra("keyIdentifier", value)
                startActivity(intent)
            }
        })

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        this.intent = intent
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        Log.e("SearchableActivity", "handleIntent called " + intent?.action)
        if (Intent.ACTION_SEARCH == intent?.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            Log.e("SearchableActivity", "query ${query}")
            if (query != null) {
                doMySearch(query)
            }
        }
    }

    private fun doMySearch(query: String) {
        val books = database.searchBooks(query)
        val adapter = SearchAdapter(this, books)
        val listView = findViewById<ListView>(R.id.listView)
        listView.setAdapter(adapter)

    }
}
