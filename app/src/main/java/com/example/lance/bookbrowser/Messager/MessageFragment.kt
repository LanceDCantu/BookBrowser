package com.example.lance.bookbrowser.Messager

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.lance.bookbrowser.BookInfoStore
import com.example.lance.bookbrowser.MyInterests.MyInterestsFragment
import com.example.lance.bookbrowser.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.lance.bookbrowser.Cart.CustomCartAdapter.ClickListener
import com.example.lance.bookbrowser.Message
import com.example.lance.bookbrowser.UserData
import kotlinx.android.synthetic.main.activity_main_search.*
import kotlinx.android.synthetic.main.product_item.view.*

class MessageFragment : Fragment() {

    val users = FirebaseDatabase.getInstance("https://bookbrowser-9108e-users.firebaseio.com").reference
    val market = FirebaseDatabase.getInstance("https://bookbrowser-9108e-users.firebaseio.com").reference
    val myUser = UserData.getData()

    var market_id: String = "null"

    enum class LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    lateinit var mCurrentLayoutManagerType: LayoutManagerType

    var mDatasetMessages: Array<Message?> = arrayOfNulls(0)

    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: CustomMessageAdapter
    lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = arguments
        val market_id = args!!.getString("id")

        initDataset()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.message_content, container, false)
        rootView.setTag(TAG)

        mRecyclerView = rootView.findViewById(R.id.messagesRecyclerView)

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = LinearLayoutManager(activity)
        mRecyclerView.layoutManager = mLayoutManager
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER

        mAdapter = CustomMessageAdapter(
            mDatasetMessages
        )

        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.adapter = mAdapter
        // END_INCLUDE(initializeRecyclerView)

        return rootView
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType)
        super.onSaveInstanceState(savedInstanceState)
    }

    private fun initDataset()
    {
        val menuListener = object : ValueEventListener
        {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                val size : Int = dataSnapshot.childrenCount.toInt()
                mDatasetMessages = arrayOfNulls(size)

                for (message in dataSnapshot.children)
                {
                    //Read the message and possibly store display mode
                }

                mAdapter.notifyDataSetChanged()
                mAdapter = CustomMessageAdapter(
                    mDatasetMessages
                )

                mRecyclerView.adapter = mAdapter
            }
            override fun onCancelled(databaseError: DatabaseError)
            {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }

        //marketid + /conversations/ + buyer (since we have the seller stored outside
        market.child(market_id + "/conversations/" + "/" + myUser + "/").addValueEventListener(menuListener)
    }


    interface OnCartEntryListener {
        fun SetMonetaryValuesCart(monetaryValues : DoubleArray)
    }

    companion object {

        private val TAG = "RecyclerViewFragment"
        private val KEY_LAYOUT_MANAGER = "layoutManager"
        private val SPAN_COUNT = 2
        private val DATASET_COUNT = 60
    }
}


