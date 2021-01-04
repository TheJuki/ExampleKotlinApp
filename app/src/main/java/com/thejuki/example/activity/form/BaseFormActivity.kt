package com.thejuki.example.activity.form

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.thejuki.example.R
import com.thejuki.example.activity.BaseActivity
import com.thejuki.example.databinding.ActivityFormBinding
import io.reactivex.disposables.Disposable

/**
 * Base Form Activity
 *
 * Handles the common tasks of form activities.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
abstract class BaseFormActivity<T : Parcelable> : BaseActivity() {
    protected var disposable: Disposable? = null
    protected var mItem: T? = null
    private val logTag = "BaseForm"
    protected lateinit var binding: ActivityFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(ARG_ITEM)) {
                mItem = savedInstanceState.getParcelable(ARG_ITEM)
            }
        } else {
            val b = intent.extras
            if (b != null) {
                mItem = b.getParcelable(ARG_ITEM)
            }
        }
    }

    abstract fun setupForm()
    abstract fun save()
    abstract fun validateForm(): Boolean

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                R.id.action_save -> {
                    save()
                    true
                }
                android.R.id.home -> {
                    showCancelConfirm()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    override fun onBackPressed() {
        showCancelConfirm()
    }

    private fun showCancelConfirm() {
        AlertDialog.Builder(this)
                .setTitle(this.getString(R.string.discard_confirm_title))
                .setMessage(this.getString(R.string.discard_confirm_description))
                .setPositiveButton(this.getString(android.R.string.ok)) { _, _ ->
                    finish()
                }
                .setNegativeButton(this.getString(android.R.string.cancel)) { _, _ ->
                }
                .create().show()
    }

    protected fun showProgressBar() {
        hideSoftKeyBoard()
        binding.progressbar.visibility = View.VISIBLE
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun hideSoftKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (imm.isAcceptingText) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    protected fun hideProgressBar() {
        binding.progressbar.visibility = View.GONE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(ARG_ITEM, mItem)
        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    companion object {
        const val ARG_ITEM = "item"
    }
}
