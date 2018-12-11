package com.example.lance.bookbrowser

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.example.lance.bookbrowser.model.Book

class SearchAdapter(context: Context, books: List<Book>) : BaseAdapter() {

   private val layoutInflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
   private val bookStorage = books

    override fun getCount(): Int {
        return bookStorage.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }
    override fun getItem(position: Int): Any {
        return bookStorage.get(position);
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val listViewHolder: ViewHolder
        val workingView: View
        if (convertView == null) {
            workingView = layoutInflator.inflate(R.layout.list_item, parent, false)
            listViewHolder = ViewHolder(workingView.findViewById(R.id.list_item_search) as TextView)
            workingView.tag = listViewHolder
        } else {
            workingView = convertView
            listViewHolder = workingView.tag as ViewHolder
        }
        listViewHolder.view.setText(bookStorage.get(position).title)
        return workingView

    }

    data class ViewHolder(val view: TextView)
}