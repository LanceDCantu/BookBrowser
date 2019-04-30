package com.example.lance.bookbrowser.StoreLocater

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.algolia.instantsearch.ui.utils.ItemClickSupport
import com.example.lance.bookbrowser.*
import com.example.lance.bookbrowser.Cart.Cart
import com.example.lance.bookbrowser.Cart.CartFragment

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main_search.*
import kotlinx.android.synthetic.main.activity_store_locater.*
import kotlinx.android.synthetic.main.product_item.view.*

class StoreLocater : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

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
        setContentView(R.layout.activity_store_locater)

        navigation_stores.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_stores)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        bottomNavigation.getMenu().findItem(R.id.navigation_stores).setChecked(true)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            val fragment = StoreMenuFragment()
            transaction.replace(R.id.stores_sub_menu, fragment)
            transaction.commit()
        }
    /*
        ItemClickSupport.addTo(hits).setOnItemClickListener(object : ItemClickSupport.OnItemClickListener {
            override fun onItemClick(recyclerView: RecyclerView, position: Int, v: View) {
                var clicked_view : View = hits.getChildAt(position)
                var isbn_clicked : String? = clicked_view.book_isbn.text.toString()

                val intent = Intent(this@StoreLocater, StoreInfoActivity::class.java)
                intent.putExtra("book_isbn", isbn_clicked)
                startActivity(intent)
            }
        })
        */
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        //val sydney = LatLng(-34.0, 151.0)
        val reno = LatLng(39.5296, -119.8138)
        val grass_roots = LatLng(39.5003359, -119.7868823)
        mMap.addMarker(MarkerOptions().position(grass_roots).title("Grassroots Books"))

        val book_gallery = LatLng(39.5439247, -119.7639036)
        mMap.addMarker(MarkerOptions().position(book_gallery).title("Book Gallery"))

        val sundance = LatLng(39.5204429, -119.814661)
        mMap.addMarker(MarkerOptions().position(sundance).title("Sundance Books"))
        //mMap.addMarker(MarkerOptions().position(grass_roots))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(reno, 12.0f))

        mMap.setOnMarkerClickListener {
            Toast.makeText(this@StoreLocater, it.title, Toast.LENGTH_SHORT).show()
            return@setOnMarkerClickListener false
        }


    }
}
