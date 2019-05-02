package com.example.lance.bookbrowser.Messager

import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lance.bookbrowser.Message
import com.example.lance.bookbrowser.R
import kotlinx.android.synthetic.main.message_layout.view.*


//Provide views to RecyclerView with data from our data sets created in the ProductFragment class.

class CustomMessageAdapter

//Initialize the dataset of the Adapter.

(private val mDataSetMessageText: Array<String?>, private val mDataSetSender: Array<Boolean?>) :
RecyclerView.Adapter<CustomMessageAdapter.ViewHolder>() {

    //Provide a reference to the type of views that you are using (custom ViewHolder)

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v){

        var messageTextView: TextView = v.findViewById<View>(R.id.message_text) as TextView
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view.
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.message_layout, viewGroup, false)
        return ViewHolder(v)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.messageTextView.text = mDataSetMessageText[position]!!

        if(mDataSetSender[position] == true) {
            //move the layout to the right
            viewHolder.itemView.message_layout.gravity = Gravity.RIGHT
        }
        else
        {
            viewHolder.itemView.message_layout.gravity = Gravity.LEFT
        }
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return mDataSetMessageText.size
    }
}
