package com.thejuki.example.activity.detail

import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.thejuki.example.R
import com.thejuki.example.activity.BaseActivity
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_item_detail.*

/**
 * Base Detail Activity
 *
 * Handles the common tasks of detail activities.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
abstract class BaseDetailActivity<T> : BaseActivity() {
    private var id: String? = null
    protected var disposable: Disposable? = null

    protected var mItem: T? = null
    protected var mBottomSheetDialog: BottomSheetDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Setup Bottom Sheet Dialog
        setupBottomSheet()

        if (savedInstanceState != null) {
            progressbar.visibility = View.GONE
            if (savedInstanceState.containsKey(ARG_ITEM_ID)) {
                id = savedInstanceState.getString(ARG_ITEM_ID)
            }
        } else {
            val b = intent.extras
            if (b != null) {
                id = b.getString(ARG_ITEM_ID)
            }
        }

        supportActionBar?.title = "Loading..."
        getItem(id.orEmpty())
    }

    abstract fun setupBottomSheet()
    abstract fun setupTabs()
    abstract fun getItem(id: String)

    public override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(ARG_ITEM_ID, id.orEmpty())
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBottomSheetDialog?.dismiss()
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                R.id.action_edit -> {
                    mBottomSheetDialog!!.show()
                    true
                }
                android.R.id.home -> {
                    finish()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    companion object {
        const val ARG_ITEM_ID = "item_id"
    }
}