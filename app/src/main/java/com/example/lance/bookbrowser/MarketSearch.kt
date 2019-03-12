package com.example.lance.bookbrowser

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.algolia.instantsearch.core.helpers.Searcher
import com.algolia.instantsearch.ui.helpers.InstantSearch
import com.algolia.instantsearch.ui.utils.ItemClickSupport
import com.example.lance.bookbrowser.Cart.Cart
import kotlinx.android.synthetic.main.activity_market_search.*


class MarketSearchActivity : AppCompatActivity() {

    lateinit var searcher: Searcher
    lateinit var helper: InstantSearch

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
        setContentView(R.layout.activity_market_search)

        navigation_market_search.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_market_search)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        bottomNavigation.getMenu().findItem(R.id.navigation_user_market).setChecked(true)

        market_hits.isClickable = true

        ItemClickSupport.addTo(market_hits).setOnItemClickListener(object : ItemClickSupport.OnItemClickListener {
            override fun onItemClick(recyclerView: RecyclerView, position: Int, v: View) {
                Toast.makeText(this@MarketSearchActivity, "we did it!", Toast.LENGTH_SHORT).show()
            }
        })

        val quick_launch = findViewById<Button>(R.id.market_quick_launch_book_button)

        // set on-click listener
        quick_launch.setOnClickListener {
            val intent = Intent(this, BookInfoMarket::class.java)
            intent.putExtra("book_isbn", "9789402306538")
            startActivity(intent)
        }

        searcher = Searcher.create("BFI0J5Z2YU","4d6cd0c8a3cb8de4ecf73dd0389a53e0", "market")
        helper = InstantSearch(this, searcher)
        helper.search()
    }

    override fun onDestroy() {
        searcher.destroy()
        super.onDestroy()
    }
}

