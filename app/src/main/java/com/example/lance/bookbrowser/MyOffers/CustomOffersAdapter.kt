package com.example.lance.bookbrowser.MyInterests

import android.app.PendingIntent.getActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lance.bookbrowser.R
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import com.example.lance.bookbrowser.BookInfoMarket


//Provide views to RecyclerView with data from our data sets created in the ProductFragment class.

class CustomOffersAdapter

//Initialize the dataset of the Adapter.

    (private val mDataSetTitle: Array<String?>, private val mDataSetAuthor: Array<String?>,
     private val mDataSetPrice: Array<String?>, private val mDataSetSeller: Array<String?>) :
    RecyclerView.Adapter<CustomOffersAdapter.ViewHolder>() {

    //Provide a reference to the type of views that you are using (custom ViewHolder)
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        var bookTitleTextView: TextView = v.findViewById<View>(R.id.book_title) as TextView
        var bookAuthorTextView: TextView = v.findViewById<View>(R.id.book_author) as TextView
        var bookPriceTextView: TextView = v.findViewById<View>(R.id.book_price) as TextView
        var bookSellerTextView: TextView = v.findViewById<View>(R.id.book_seller) as TextView

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            CustomOffersAdapter.clickListener!!.onItemClick(getAdapterPosition(), view)

            //val intent = Intent(view.context, BookInfoMarket::class.java)
            //startActivity(intent)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view.
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.product_item_market, viewGroup, false)

        return ViewHolder(v)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.bookTitleTextView.text = mDataSetTitle[position]
        viewHolder.bookAuthorTextView.text = mDataSetAuthor[position]
        viewHolder.bookPriceTextView.text = mDataSetPrice[position]
        viewHolder.bookSellerTextView.text = mDataSetSeller[position]
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return mDataSetTitle.size
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        CustomOffersAdapter.clickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(position: Int, v: View)
    }

    companion object {
        private var clickListener: CustomOffersAdapter.ClickListener? = null
    }
}
