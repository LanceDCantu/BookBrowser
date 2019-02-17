/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.lance.bookbrowser

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton

/**
 * Demonstrates the use of [RecyclerView] with a [LinearLayoutManager] and a
 * [GridLayoutManager].
 */
@SuppressLint("ValidFragment")
class RecyclerViewFragment (private val dataInView : Array<Book>) : Fragment() {

    private var mCurrentLayoutManagerType: LayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER

    protected lateinit var mRecyclerView: RecyclerView
    protected lateinit var mAdapter: CustomAdapter
    protected lateinit var mLayoutManager: RecyclerView.LayoutManager

    protected lateinit var mDatasetTitle: Array<String?>
    protected lateinit var mDatasetAuthor: Array<String?>
    protected lateinit var mDatasetPrice: Array<String?>
    protected lateinit var mDatasetReviews: Array<String?>

    private enum class LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
        initDataset()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.recycler_view_fragment, container, false)
        rootView.setTag(TAG)

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = rootView.findViewById(R.id.recyclerView)

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = LinearLayoutManager(activity)

        mCurrentLayoutManagerType =
                LayoutManagerType.LINEAR_LAYOUT_MANAGER

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = savedInstanceState
                .getSerializable(KEY_LAYOUT_MANAGER) as LayoutManagerType
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType)


        mAdapter = CustomAdapter(mDatasetTitle ,mDatasetAuthor, mDatasetPrice, mDatasetReviews)

        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.adapter = mAdapter
        // END_INCLUDE(initializeRecyclerView)

        return rootView
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    private fun setRecyclerViewLayoutManager(layoutManagerType: LayoutManagerType) {
        var scrollPosition = 0

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.layoutManager != null) {
            scrollPosition = (mRecyclerView.layoutManager as LinearLayoutManager)
                .findFirstCompletelyVisibleItemPosition()
        }

        when (layoutManagerType) {
            LayoutManagerType.GRID_LAYOUT_MANAGER -> {
                mLayoutManager = GridLayoutManager(activity,
                    SPAN_COUNT
                )
                mCurrentLayoutManagerType =
                        LayoutManagerType.GRID_LAYOUT_MANAGER
            }
            LayoutManagerType.LINEAR_LAYOUT_MANAGER -> {
                mLayoutManager = LinearLayoutManager(activity)
                mCurrentLayoutManagerType =
                        LayoutManagerType.LINEAR_LAYOUT_MANAGER
            }

            else -> {
                mLayoutManager = LinearLayoutManager(activity)
                mCurrentLayoutManagerType =
                        LayoutManagerType.LINEAR_LAYOUT_MANAGER
            }
        }

        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.scrollToPosition(scrollPosition)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType)
        super.onSaveInstanceState(savedInstanceState)
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    public fun initDataset() {

        mDatasetTitle = arrayOfNulls(DATASET_COUNT)
        mDatasetAuthor = arrayOfNulls(DATASET_COUNT)
        mDatasetPrice = arrayOfNulls(DATASET_COUNT)
        mDatasetReviews = arrayOfNulls(DATASET_COUNT)

        for (i in 0 until 60) {
            mDatasetTitle[i] = "This is element #$i (title)"
            mDatasetAuthor[i] = "This is element #$i (author)"
            mDatasetPrice[i] = "This is element #$i (price)"
            mDatasetReviews[i] = "This is element #$i (reviews)"
        }

        if(dataInView != null)
        {
            for (i in 0 until 60) {
                mDatasetTitle[i] = dataInView[i].title
                mDatasetAuthor[i] = dataInView[i].author
                mDatasetPrice[i] = "$" + dataInView[i].cost.toString()
                mDatasetReviews[i] = dataInView[i].reviews.toString() + " Reviews"
            }
        }
    }

    companion object {

        private val TAG = "RecyclerViewFragment"
        private val KEY_LAYOUT_MANAGER = "layoutManager"
        private val SPAN_COUNT = 2
        private val DATASET_COUNT = 60
    }
}


