/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.lance.bookbrowser

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import com.algolia.instantsearch.core.helpers.Searcher
import com.algolia.instantsearch.ui.helpers.InstantSearch
import kotlinx.android.synthetic.main.activity_main_search.*
import com.algolia.instantsearch.ui.utils.ItemClickSupport
import com.example.lance.bookbrowser.Cart.Cart
import com.example.lance.bookbrowser.StoreLocater.StoreLocater
import kotlinx.android.synthetic.main.product_item.view.*


class MainSearchActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_main_search)

        navigation_search.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_search)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        bottomNavigation.getMenu().findItem(R.id.navigation_browse).setChecked(true)

        hits.isClickable = true

        ItemClickSupport.addTo(hits).setOnItemClickListener(object : ItemClickSupport.OnItemClickListener {
            override fun onItemClick(recyclerView: RecyclerView, position: Int, v: View) {
                var isbn_clicked : String? = v.book_isbn.text.toString()

                val intent = Intent(this@MainSearchActivity, BookInfoStore::class.java)
                intent.putExtra("book_isbn", isbn_clicked)
                startActivity(intent)
            }
        })

        searcher = Searcher.create("BFI0J5Z2YU","4d6cd0c8a3cb8de4ecf73dd0389a53e0", "products")
        helper = InstantSearch(this, searcher)
        helper.search()
    }

    override fun onDestroy() {
        searcher.destroy()
        super.onDestroy()
    }
}

