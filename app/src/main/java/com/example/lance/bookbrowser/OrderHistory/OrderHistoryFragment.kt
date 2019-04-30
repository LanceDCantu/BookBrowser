package com.example.lance.bookbrowser.OrderHistory

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.lance.bookbrowser.Book
import com.example.lance.bookbrowser.MyInterests.MyInterestsFragment
import com.example.lance.bookbrowser.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.lance.bookbrowser.Cart.CustomCartAdapter.ClickListener
import com.example.lance.bookbrowser.UserData

class OrderHistoryFragment : Fragment() {

    val users_ref = FirebaseDatabase.getInstance("https://bookbrowser-9108e-users.firebaseio.com").reference
    val myUser = UserData.getData()

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
    lateinit var mAdapter: CustomOrderHistoryAdapter
    lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initHistoryData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.content_order_history, container, false)
        rootView.setTag(TAG)

        mRecyclerView = rootView.findViewById(R.id.orderHistoryRecyclerView)

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = LinearLayoutManager(activity)
        mRecyclerView.layoutManager = mLayoutManager
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER

        mAdapter = CustomOrderHistoryAdapter(
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

    //private fun initBookData(layoutInflater: LayoutInflater, viewGroup: ViewGroup?): View
    private fun initHistoryData() {

        //val rowMain = layoutInflater.inflate(R.layout.row_main, viewGroup, false)
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                val size : Int = dataSnapshot.childrenCount.toInt()

                mDatasetTitle = arrayOfNulls(size)
                mDatasetAuthor = arrayOfNulls(size)
                mDatasetPrice = arrayOfNulls(size)
                mDatasetStore = arrayOfNulls(size)

                var index: Int = 0

                for (book in dataSnapshot.children)
                {
                    mDatasetTitle[index] = book.child("/title/").value.toString()
                    mDatasetAuthor[index] = book.child("/author/").value.toString()
                    mDatasetPrice[index] = book.child("/price/").value.toString()
                    mDatasetStore[index] = book.child("/store/").value.toString()


                    index ++
                }

                mAdapter.notifyDataSetChanged()
                mAdapter = CustomOrderHistoryAdapter(
                    mDatasetTitle,
                    mDatasetAuthor,
                    mDatasetPrice,
                    mDatasetStore
                )

                mRecyclerView.adapter = mAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }

        }
        users_ref.child(myUser + "/order_history/").addListenerForSingleValueEvent(userListener)
    }

    companion object {

        private val TAG = "RecyclerViewFragment"
        private val KEY_LAYOUT_MANAGER = "layoutManager"
        private val SPAN_COUNT = 2
        private val DATASET_COUNT = 60
    }
}


