package com.example.lance.bookbrowser

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


//Provide views to RecyclerView with data from our data sets created in the ProductFragment class.

class CustomAdapter
/**
 * Initialize the dataset of the Adapter.
 *
 * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
 */
    (private val mDataSetTitle: Array<String?>, private val mDataSetAuthor: Array<String?>,
        private val mDataSetPrice: Array<String?>, private val mDataSetReviews: Array<String?>) :
        RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var bookTitleTextView: TextView = v.findViewById<View>(R.id.book_title) as TextView
        var bookAuthorTextView: TextView = v.findViewById<View>(R.id.book_author) as TextView
        var bookPriceTextView: TextView = v.findViewById<View>(R.id.book_price) as TextView
        var bookReviewTextView: TextView = v.findViewById<View>(R.id.book_review) as TextView
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view.
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.product_item, viewGroup, false)

        return ViewHolder(v)
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.bookTitleTextView.text = mDataSetTitle[position]
        viewHolder.bookAuthorTextView.text = mDataSetAuthor[position]
        viewHolder.bookPriceTextView.text = mDataSetPrice[position]
        viewHolder.bookReviewTextView.text = mDataSetReviews[position]
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return mDataSetTitle.size
    }

    companion object {
        private val TAG = "CustomAdapter"
    }
}
