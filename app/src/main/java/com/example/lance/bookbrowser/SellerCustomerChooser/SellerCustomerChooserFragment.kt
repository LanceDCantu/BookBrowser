package com.example.lance.bookbrowser.SellerCustomerChooser

import android.content.Intent
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
import com.example.lance.bookbrowser.Messager.MessagingActivity
import com.example.lance.bookbrowser.UserData
import kotlinx.android.synthetic.main.customer_item.view.*

class SellerCustomerChooserFragment : Fragment() {

    //val users = FirebaseDatabase.getInstance("https://bookbrowser-9108e-users.firebaseio.com").reference
    val market = FirebaseDatabase.getInstance("https://bookbrowser-9108e-market.firebaseio.com").reference
    val myUser = UserData.getData()

    var market_id: String = "null"

    enum class LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    lateinit var mCurrentLayoutManagerType: LayoutManagerType

    var mDatasetBuyers: Array<String?> = arrayOfNulls(0)

    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: CustomSellerCustomerChooserAdapter
    lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = arguments
        market_id = args!!.getString("id")

        initDataset()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.seller_customer_chooser_content, container, false)
        rootView.setTag(TAG)

        mRecyclerView = rootView.findViewById(R.id.sellerCustomerChooserRecyclerView)

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = LinearLayoutManager(activity)
        mRecyclerView.layoutManager = mLayoutManager
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER

        mAdapter = CustomSellerCustomerChooserAdapter(
            mDatasetBuyers
        )

        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.adapter = mAdapter
        // END_INCLUDE(initializeRecyclerView)

        mAdapter.setOnItemClickListener(object : CustomSellerCustomerChooserAdapter.ClickListener {
            override fun onItemClick(position: Int, v: View) {
                var buyer_name = v.buyer_text.text

                val intent = Intent(activity, MessagingActivity::class.java)
                intent.putExtra("id", market_id)
                intent.putExtra("buyer", buyer_name)
                startActivity(intent)
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
                val size : Int = dataSnapshot.childrenCount.toInt()
                mDatasetBuyers = arrayOfNulls(size)

                var index = 0

                for (buyer_snap in dataSnapshot.children)
                {
                    mDatasetBuyers[index] = buyer_snap.key.toString()

                    index++
                }

                mAdapter.notifyDataSetChanged()
                mAdapter = CustomSellerCustomerChooserAdapter(
                    mDatasetBuyers
                )

                mRecyclerView.adapter = mAdapter
            }
            override fun onCancelled(databaseError: DatabaseError)
            {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        market.child(market_id + "/conversations/").addValueEventListener(menuListener)
    }

    companion object {

        private val TAG = "RecyclerViewFragment"
        private val KEY_LAYOUT_MANAGER = "layoutManager"
        private val SPAN_COUNT = 2
        private val DATASET_COUNT = 60
    }
}


