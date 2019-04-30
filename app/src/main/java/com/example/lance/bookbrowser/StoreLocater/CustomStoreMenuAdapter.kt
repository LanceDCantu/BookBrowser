package com.example.lance.bookbrowser.StoreLocater

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lance.bookbrowser.R


//Provide views to RecyclerView with data from our data sets created in the ProductFragment class.

class CustomStoreMenuAdapter

//Initialize the dataset of the Adapter.

    (private val mDataSetStoreName: Array<String?>, private val mDataSetHours: Array<String?>,
     private val mDataSetDistance: Array<String?>) : RecyclerView.Adapter<CustomStoreMenuAdapter.ViewHolder>() {

    //Provide a reference to the type of views that you are using (custom ViewHolder)

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        var storeNameTextView: TextView = v.findViewById<View>(R.id.store_name) as TextView
        var storeHoursTextView: TextView = v.findViewById<View>(R.id.store_hours) as TextView
        //var bookPriceTextView: TextView = v.findViewById<View>(R.id.book_price) as TextView

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            clickListener!!.onItemClick(getAdapterPosition(), view)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view.
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.store_item, viewGroup, false)
        return ViewHolder(v)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.storeNameTextView.text = mDataSetStoreName[position]
        viewHolder.storeHoursTextView.text = mDataSetHours[position]
        //viewHolder.bookPriceTextView.text = mDataSetDistance[position]
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return mDataSetStoreName.size
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        CustomStoreMenuAdapter.clickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(position: Int, v: View)
    }

    companion object {
        private var clickListener: CustomStoreMenuAdapter.ClickListener? = null
    }
}
