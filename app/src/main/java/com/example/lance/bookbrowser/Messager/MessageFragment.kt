package com.example.lance.bookbrowser.Messager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lance.bookbrowser.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.lance.bookbrowser.Message
import com.example.lance.bookbrowser.UserData

class MessageFragment : Fragment() {

    val users = FirebaseDatabase.getInstance("https://bookbrowser-9108e-users.firebaseio.com").reference
    val market = FirebaseDatabase.getInstance("https://bookbrowser-9108e-market.firebaseio.com").reference
    val myUser = UserData.getData()

    var market_id: String = "null"
    var snapshotDir: String = "null"

    enum class LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    lateinit var mCurrentLayoutManagerType: LayoutManagerType

    var mDatasetMessageText: Array<String?> = arrayOfNulls(0)
    var mDatasetMessageSender: Array<Boolean?> = arrayOfNulls(0)

    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: CustomMessageAdapter
    lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = arguments
        val market_id = args!!.getString("id")
        val buyer:String? = args.getString("buyer")

        //we need to use the name of the buyer if we are the one selling the book. This is be dependant on what activity we are coming from
        if(buyer != null && buyer != "")
        {
            //then we are the seller
            snapshotDir = market_id + "/conversations/" + buyer + "/"
        }
        else
        {
            //then we are the buyer
            snapshotDir = market_id + "/conversations/" + buyer + "/"
        }

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
        mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true)
        mRecyclerView.layoutManager = mLayoutManager
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER

        mDatasetMessageText.reverse()
        mDatasetMessageSender.reverse()

        mAdapter = CustomMessageAdapter(
            mDatasetMessageText,
            mDatasetMessageSender
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
                mDatasetMessageText = arrayOfNulls(size)
                mDatasetMessageSender = arrayOfNulls(size)

                var index = 0

                for (message_snap in dataSnapshot.children)
                {
                    //Read the message and possibly store display mode
                    mDatasetMessageText[index] = message_snap.child("/data/").value.toString()
                    mDatasetMessageSender[index] = message_snap.child("/sender/").value as Boolean

                    index++
                }

                mDatasetMessageText.reverse()
                mDatasetMessageSender.reverse()

                mAdapter.notifyDataSetChanged()
                mAdapter = CustomMessageAdapter(
                    mDatasetMessageText,
                    mDatasetMessageSender
                )

                mRecyclerView.adapter = mAdapter

                mRecyclerView.scrollToPosition(size - 1)
            }
            override fun onCancelled(databaseError: DatabaseError)
            {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        market.child(snapshotDir).addValueEventListener(menuListener)
    }

    companion object {

        private val TAG = "RecyclerViewFragment"
        private val KEY_LAYOUT_MANAGER = "layoutManager"
        private val SPAN_COUNT = 2
        private val DATASET_COUNT = 60
    }
}


