package com.example.lance.bookbrowser.messaging

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.lance.bookbrowser.R
import com.example.lance.bookbrowser.UserData
import com.google.firebase.database.*
import com.google.firebase.database.DataSnapshot


class MyMessagingFragment : Fragment() {

    val users_ref = FirebaseDatabase.getInstance("https://bookbrowser-9108e-users.firebaseio.com").reference
    val market_ref = FirebaseDatabase.getInstance("https://bookbrowser-9108e-market.firebaseio.com").reference
    val myUser = UserData.getData()

    enum class LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    lateinit var mCurrentLayoutManagerType: LayoutManagerType

    var mDatasetTitle: Array<String?> = arrayOfNulls(0)
    var mDatasetAuthor: Array<String?> = arrayOfNulls(0)
    var mDatasetPrice: Array<String?> = arrayOfNulls(0)
    var mDatasetSeller: Array<String?> = arrayOfNulls(0)

    var InterestIDs: Array<String?> = arrayOfNulls(0)

    var num_user_interests : Int = 0

    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: CustomMessagingAdapter
    lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDataset()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.content_my_interests, container, false)
        rootView.setTag(TAG)

        mRecyclerView = rootView.findViewById(R.id.myInterestsRecyclerView)

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = LinearLayoutManager(activity)
        mRecyclerView.layoutManager = mLayoutManager
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER

        mAdapter = CustomMessagingAdapter(
            mDatasetTitle,
            mDatasetAuthor,
            mDatasetPrice,
            mDatasetSeller

        )
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.adapter = mAdapter
        // END_INCLUDE(initializeRecyclerView)

        mAdapter.setOnItemClickListener(object : CustomMessagingAdapter.ClickListener {
            override fun onItemClick(position: Int, v: View) {
                Toast.makeText(activity, "we did it!", Toast.LENGTH_SHORT).show()
            }
        })

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
                num_user_interests = dataSnapshot.childrenCount.toInt()
                InterestIDs = arrayOfNulls(num_user_interests)

                var index = 0

                for (interest_item_snap in dataSnapshot.children)
                {
                    InterestIDs[index] = interest_item_snap.value.toString()

                    index++
                }

                readMarketData()
            }
            override fun onCancelled(databaseError: DatabaseError)
            {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        //this is how we query for the specific user, we need to make the "lancedcantu" dynamic
        //we only want to a do a query if the user updates not the market
        users_ref.child(myUser + "/" + "my_interests/").addValueEventListener(menuListener)
    }

    private fun readMarketData()
    {
        market_ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                mDatasetTitle = arrayOfNulls(num_user_interests)
                mDatasetAuthor = arrayOfNulls(num_user_interests)
                mDatasetPrice = arrayOfNulls(num_user_interests)
                mDatasetSeller = arrayOfNulls(num_user_interests)

                var index = 0

                for(interest_id in InterestIDs)
                {
                    //get a point query here
                    //if something can't load put can't load

                    mDatasetTitle[index] = dataSnapshot.child("/" + interest_id + "/title/").value.toString()
                    mDatasetPrice[index] = dataSnapshot.child("/" + interest_id + "/asking_price/").value.toString()
                    mDatasetSeller[index] = dataSnapshot.child("/" + interest_id + "/seller/").value.toString()
                    mDatasetAuthor[index] = dataSnapshot.child("/" + interest_id + "/authors/").value.toString()

                    index++
                }

                mAdapter.notifyDataSetChanged()
                mAdapter = CustomMessagingAdapter(
                    mDatasetTitle,
                    mDatasetAuthor,
                    mDatasetPrice,
                    mDatasetSeller
                )
                mRecyclerView.adapter = mAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    companion object {

        private val TAG = "MyMessagingFragment"
        private val KEY_LAYOUT_MANAGER = "layoutManager"
        private val SPAN_COUNT = 2
        private val DATASET_COUNT = 60
    }
}


