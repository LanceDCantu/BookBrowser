package com.example.lance.bookbrowser.SellerCustomerChooser

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lance.bookbrowser.Message
import com.example.lance.bookbrowser.R


//Provide views to RecyclerView with data from our data sets created in the ProductFragment class.

class CustomSellerCustomerChooserAdapter

//Initialize the dataset of the Adapter.

(private val mDataBuyers: Array<String?>) :
RecyclerView.Adapter<CustomSellerCustomerChooserAdapter.ViewHolder>() {

    //Provide a reference to the type of views that you are using (custom ViewHolder)

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener{

        var buyerTextView: TextView = v.findViewById<View>(R.id.buyer_text) as TextView

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            CustomSellerCustomerChooserAdapter.clickListener!!.onItemClick(getAdapterPosition(), view)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view.
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.customer_item, viewGroup, false)
        return ViewHolder(v)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.buyerTextView.text = mDataBuyers[position]!!
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return mDataBuyers.size
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        CustomSellerCustomerChooserAdapter.clickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(position: Int, v: View)
    }

    companion object {
        private var clickListener: CustomSellerCustomerChooserAdapter.ClickListener? = null
    }
}
