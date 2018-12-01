package com.thejuki.example.adapter.view

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * Recycler View Empty Support
 *
 * Adds the setEmptyView function to RecyclerView.
 * @see ([How to show an empty view with a RecyclerView?](https://stackoverflow.com/a/30415582/4056401))
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class RecyclerViewEmptySupport : androidx.recyclerview.widget.RecyclerView {
    private var emptyView: View? = null

    private val emptyObserver = object : androidx.recyclerview.widget.RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            val adapter = adapter
            if (adapter != null && emptyView != null) {
                if (adapter.itemCount == 0) {
                    emptyView!!.visibility = View.VISIBLE
                    this@RecyclerViewEmptySupport.visibility = View.GONE
                } else {
                    emptyView!!.visibility = View.GONE
                    this@RecyclerViewEmptySupport.visibility = View.VISIBLE
                }
            }
        }
    }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun setAdapter(adapter: androidx.recyclerview.widget.RecyclerView.Adapter<*>?) {
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(emptyObserver)
        emptyObserver.onChanged()
    }

    fun setEmptyView(emptyView: View) {
        this.emptyView = emptyView
    }
}