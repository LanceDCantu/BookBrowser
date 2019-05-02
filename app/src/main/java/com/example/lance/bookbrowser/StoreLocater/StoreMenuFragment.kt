package com.example.lance.bookbrowser.StoreLocater

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lance.bookbrowser.R
import kotlinx.android.synthetic.main.store_item.view.*

class StoreMenuFragment : Fragment() {

    //val secondary = FirebaseDatabase.getInstance("https://bookbrowser-9108e-users.firebaseio.com").reference

    enum class LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    lateinit var mCurrentLayoutManagerType: LayoutManagerType

    var mDatasetStoreName: Array<String?> = arrayOfNulls(0)
    var mDatasetHours: Array<String?> = arrayOfNulls(0)
    var mDatasetDistance: Array<String?> = arrayOfNulls(0)

    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: CustomStoreMenuAdapter
    lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDataset()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.content_store_locater_sub_menu, container, false)
        rootView.setTag(TAG)

        mRecyclerView = rootView.findViewById(R.id.storeRecyclerView)

        mLayoutManager = LinearLayoutManager(activity)
        mRecyclerView.layoutManager = mLayoutManager
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER

        mAdapter = CustomStoreMenuAdapter(
            mDatasetStoreName,
            mDatasetHours,
            mDatasetDistance
        )

        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.adapter = mAdapter
        // END_INCLUDE(initializeRecyclerView)

        mAdapter.setOnItemClickListener(object : CustomStoreMenuAdapter.ClickListener {
            override fun onItemClick(position: Int, v: View) {
                //Toast.makeText(activity, "we did it!", Toast.LENGTH_SHORT).show()
                if(v.store_name.text == "Grassroots Books")
                {
                    //Go into the store info activity to show full data
                    val intent = Intent(activity, StoreInfoActivity::class.java)
                    startActivity(intent)
                }
                else if(v.store_name.text == "Sundance Books")
                {
                    //Go into the store info activity to show full data
                    val intent = Intent(activity, StoreInfoActivitySundance::class.java)
                    startActivity(intent)
                }
                else if (v.store_name.text == "Book Gallery Reno")
                {
                    //Go into the store info activity to show full data
                    val intent = Intent(activity, StoreInfoActivityBookGallery::class.java)
                    startActivity(intent)
                }
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
        /*
        val menuListener = object : ValueEventListener
        {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {*/
                //Eventually: we want this to be taken from  the database

                mDatasetStoreName = arrayOfNulls(3)
                mDatasetHours = arrayOfNulls(3)
                mDatasetDistance = arrayOfNulls(3)

                //Hardcode data here

                mDatasetStoreName[0] = "Grassroots Books"
                mDatasetStoreName[1] = "Sundance Books"
                mDatasetStoreName[2] = "Book Gallery Reno"

                mDatasetHours[0] = "Open: " + "9AM - 8:05PM"
                mDatasetHours[1] = "Open: " + "9AM - 9PM"
                mDatasetHours[2] = "Open: " + "10AM - 5:30PM"

                mAdapter = CustomStoreMenuAdapter(
                    mDatasetStoreName,
                    mDatasetHours,
                    mDatasetDistance
                )

                mAdapter.notifyDataSetChanged()

                //mRecyclerView.adapter = mAdapter
            /*
            }
            override fun onCancelled(databaseError: DatabaseError)
            {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        //this is how we query for the specific user, we need to make the "lancedcantu" dynamic
        secondary.child("lancedcantu@yahoo!com/" + "cart/").addValueEventListener(menuListener)
    */
    }

    companion object {

        private val TAG = "RecyclerViewFragment"
        private val KEY_LAYOUT_MANAGER = "layoutManager"
        private val SPAN_COUNT = 2
        private val DATASET_COUNT = 60
    }
}


