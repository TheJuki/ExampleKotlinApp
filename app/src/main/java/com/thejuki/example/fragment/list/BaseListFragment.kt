package com.thejuki.example.fragment.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.thejuki.example.R
import com.thejuki.example.adapter.view.ItemRecyclerViewAdapter
import com.thejuki.example.adapter.view.RecyclerViewEmptySupport
import com.thejuki.example.json.TableCellJson
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_item_list.*
import kotlinx.android.synthetic.main.fragment_item_list.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Base List Fragment
 *
 * Handles the common tasks of a list fragments.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
abstract class BaseListFragment : androidx.fragment.app.Fragment() {
    protected var searchTerm: String? = null
    protected var isSearching: Boolean = false

    protected var disposable: Disposable? = null

    private var mRecycleView: RecyclerViewEmptySupport? = null
    protected var mFab: FloatingActionButton? = null
    protected var mHeader: View? = null
    private var mSwipeRefreshLayout: androidx.swiperefreshlayout.widget.SwipeRefreshLayout? = null
    private var mEmptyView: TextView? = null
    private var mProgressBar: ProgressBar? = null
    protected var mItemRecyclerViewAdapter: ItemRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(ARG_SEARCH_TERM)) {
                searchTerm = savedInstanceState.getString(ARG_SEARCH_TERM)
            }
            if (savedInstanceState.containsKey(ARG_IS_SEARCHING)) {
                isSearching = savedInstanceState.getBoolean(ARG_IS_SEARCHING)
            }
        } else {
            if (arguments != null) {
                searchTerm = arguments!!.getString(ARG_SEARCH_TERM)
                isSearching = arguments!!.getBoolean(ARG_IS_SEARCHING)
            }
        }

        mRecycleView = view.item_list
        mFab = view.fab
        mHeader = view.header
        mSwipeRefreshLayout = view.swiperefresh
        mProgressBar = view.progressbar
        mEmptyView = view.empty
        mItemRecyclerViewAdapter = ItemRecyclerViewAdapter()
        mRecycleView!!.adapter = mItemRecyclerViewAdapter

        val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(mRecycleView!!.context,
                androidx.recyclerview.widget.DividerItemDecoration.VERTICAL)
        mRecycleView!!.addItemDecoration(dividerItemDecoration)
        mRecycleView!!.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        mRecycleView!!.setEmptyView(mEmptyView!!)

        mSwipeRefreshLayout!!.setOnRefreshListener(
                {
                    this.reload()
                }
        )
        mSwipeRefreshLayout!!.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3)

        return view
    }

    protected fun showResults(result: List<TableCellJson>, showSnackBar: Boolean = true) {
        hideLoading()
        mItemRecyclerViewAdapter!!.mValues.clear()
        mItemRecyclerViewAdapter!!.mValues.addAll(result)
        mEmptyView!!.setText(R.string.hint_NoItems)
        mItemRecyclerViewAdapter!!.notifyDataSetChanged()

        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat("h:mm a", Locale.US)

        if (showSnackBar) {
            Snackbar.make(clayout,
                    String.format(getString(R.string.list_updated), formatter.format(date),
                            mItemRecyclerViewAdapter!!.mValues.size),
                    Snackbar.LENGTH_LONG).show()
        }
    }

    protected fun showError() {
        hideLoading()
        mItemRecyclerViewAdapter!!.mValues.clear()
        mEmptyView!!.setText(R.string.server_error_description)
        mItemRecyclerViewAdapter!!.notifyDataSetChanged()
    }

    protected fun disableRefresh() {
        mSwipeRefreshLayout!!.isEnabled = false
    }

    private fun hideLoading() {
        mSwipeRefreshLayout!!.isRefreshing = false
        mProgressBar!!.visibility = View.GONE
    }

    protected fun showLoading() {
        mSwipeRefreshLayout!!.isRefreshing = true
    }

    protected abstract fun reload()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ARG_SEARCH_TERM, searchTerm)
        outState.putBoolean(ARG_IS_SEARCHING, isSearching)
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    companion object {
        const val ARG_SEARCH_TERM = "searchTerm"
        const val ARG_IS_SEARCHING = "isSearching"

        fun getBundle(searchTerm: String? = null,
                      isSearching: Boolean = false): Bundle {
            val args = Bundle()
            args.putString(ARG_SEARCH_TERM, searchTerm)
            args.putBoolean(ARG_IS_SEARCHING, isSearching)
            return args
        }
    }
}
