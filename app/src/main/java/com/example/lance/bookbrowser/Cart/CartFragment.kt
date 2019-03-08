package com.example.lance.bookbrowser.Cart

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

class CartFragment : Fragment() {

    val secondary = FirebaseDatabase.getInstance("https://bookbrowser-9108e-users.firebaseio.com").reference

    enum class LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    lateinit var mCurrentLayoutManagerType: LayoutManagerType

    var mDatasetTitle: Array<String?> = arrayOfNulls(0)
    var mDatasetAuthor: Array<String?> = arrayOfNulls(0)
    var mDatasetPrice: Array<String?> = arrayOfNulls(0)
    var mDatasetStore: Array<String?> = arrayOfNulls(0)

    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: CustomCartAdapter
    lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDataset()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.content_cart, container, false)
        rootView.setTag(TAG)

        mRecyclerView = rootView.findViewById(R.id.cartRecyclerView)

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = LinearLayoutManager(activity)
        mRecyclerView.layoutManager = mLayoutManager

        mAdapter = CustomCartAdapter(
            mDatasetTitle,
            mDatasetAuthor,
            mDatasetPrice,
            mDatasetStore
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

                mDatasetTitle = arrayOfNulls(size)
                mDatasetAuthor = arrayOfNulls(size)
                mDatasetPrice = arrayOfNulls(size)
                mDatasetStore = arrayOfNulls(size)

                var index : Int = 0

                for (cart_item_snap in dataSnapshot.children)
                {
                    mDatasetTitle[index] = cart_item_snap.child("/title/").value.toString()
                    mDatasetAuthor[index] = cart_item_snap.child("/author/").value.toString()
                    mDatasetPrice[index] = cart_item_snap.child("/price/").value.toString()
                    mDatasetStore[index] = cart_item_snap.child("/store/").value.toString()

                    index++
                }

                mAdapter.notifyDataSetChanged()
                mAdapter = CustomCartAdapter(
                    mDatasetTitle,
                    mDatasetAuthor,
                    mDatasetPrice,
                    mDatasetStore
                )
                mRecyclerView.adapter = mAdapter
            }
            override fun onCancelled(databaseError: DatabaseError)
            {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        //this is how we query for the specific user, we need to make the "lancedcantu" dynamic
        secondary.child("lancedcantu@yahoo!com/" + "cart/").addValueEventListener(menuListener)
    }

    companion object {

        private val TAG = "RecyclerViewFragment"
        private val KEY_LAYOUT_MANAGER = "layoutManager"
        private val SPAN_COUNT = 2
        private val DATASET_COUNT = 60
    }
}


