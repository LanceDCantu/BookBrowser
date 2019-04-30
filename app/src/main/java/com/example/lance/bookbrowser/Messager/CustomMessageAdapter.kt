package com.example.lance.bookbrowser.Messager

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lance.bookbrowser.Message
import com.example.lance.bookbrowser.R


//Provide views to RecyclerView with data from our data sets created in the ProductFragment class.

class CustomMessageAdapter

//Initialize the dataset of the Adapter.

(private val mDataSetMessages: Array<Message?>) :
RecyclerView.Adapter<CustomMessageAdapter.ViewHolder>() {

    //Provide a reference to the type of views that you are using (custom ViewHolder)

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v){

        var messageTextView: TextView = v.findViewById<View>(R.id.message_text) as TextView
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view.
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.product_item_cart, viewGroup, false)
        return ViewHolder(v)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.messageTextView.text = mDataSetMessages[position]!!.data
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return mDataSetMessages.size
    }
}
