package com.example.lance.bookbrowser.OrderHistory

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lance.bookbrowser.Book
import com.example.lance.bookbrowser.OrderHistory.*
import com.example.lance.bookbrowser.R


//Provide views to RecyclerView with data from our data sets created in the ProductFragment class.

class CustomOrderHistoryAdapter

//Initialize the dataset of the Adapter.

    (private val mDataSetTitle: Array<String?>, private val mDataSetAuthor: Array<String?>,
     private val mDataSetPrice: Array<String?>, private val mDataSetStore: Array<String?>) :
    RecyclerView.Adapter<CustomOrderHistoryAdapter.ViewHolder>() {

    //Provide a reference to the type of views that you are using (custom ViewHolder)

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var bookTitleTextView: TextView = v.findViewById<View>(R.id.book_title) as TextView
        var bookAuthorTextView: TextView = v.findViewById<View>(R.id.book_author) as TextView
        var bookPriceTextView: TextView = v.findViewById<View>(R.id.book_price) as TextView
        var bookStoreTextView: TextView = v.findViewById<View>(R.id.book_store) as TextView
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view.
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.product_item_order_history, viewGroup, false)
        return ViewHolder(v)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.bookTitleTextView.text = mDataSetTitle[position]
        viewHolder.bookAuthorTextView.text = mDataSetAuthor[position]
        viewHolder.bookPriceTextView.text = "$" + mDataSetPrice[position]
        viewHolder.bookStoreTextView.text = mDataSetStore[position]
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return mDataSetTitle.size
    }
}
