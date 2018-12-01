package com.thejuki.example.activity.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.thejuki.example.R
import com.thejuki.example.activity.detail.ContactDetailActivity.Tabs.*
import com.thejuki.example.activity.form.BaseFormActivity
import com.thejuki.example.activity.form.ContactFormActivity
import com.thejuki.example.activity.form.NoteFormActivity
import com.thejuki.example.api.ApiClient
import com.thejuki.example.api.AuthManager
import com.thejuki.example.enum.ObjectType
import com.thejuki.example.extension.simple
import com.thejuki.example.fragment.list.InfoListFragment
import com.thejuki.example.fragment.list.NoteListFragment
import com.thejuki.example.json.ContactJson
import com.thejuki.example.json.NoteJson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.sheet_contact.*

/**
 * Contact Detail Activity
 *
 * Contact Details
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class ContactDetailActivity : BaseDetailActivity<ContactJson>() {
    private val logTag = "ContactDetail"
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    @SuppressLint("InflateParams")
    override fun setupBottomSheet() {
        mBottomSheetDialog = BottomSheetDialog(this)
        val sheetView = this.layoutInflater.inflate(R.layout.sheet_contact, null)
        mBottomSheetDialog!!.setContentView(sheetView)

        mBottomSheetDialog!!.sheet_close.setOnClickListener({
            mBottomSheetDialog!!.dismiss()
        })

        if (AuthManager.getInstance(this).has("contact_edit")) {
            mBottomSheetDialog!!.sheet_edit.setOnClickListener({
                mBottomSheetDialog!!.dismiss()
                val intent = Intent(this, ContactFormActivity::class.java).apply {
                    putExtra(BaseFormActivity.ARG_ITEM, mItem)
                }
                startActivity(intent)
            })
        } else {
            mBottomSheetDialog!!.sheet_edit.visibility = View.GONE
        }

        if (AuthManager.getInstance(this).has("note_add")) {
            mBottomSheetDialog!!.sheet_add_note.setOnClickListener({
                mBottomSheetDialog!!.dismiss()
                val intent = Intent(this, NoteFormActivity::class.java).apply {
                    putExtra(BaseFormActivity.ARG_ITEM,
                            NoteJson(parentId = mItem!!.id,
                                    parentType = ObjectType.Contact.value))
                }
                startActivity(intent)
            })
        } else {
            mBottomSheetDialog!!.sheet_add_note.visibility = View.GONE
        }
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

    override fun getItem(id: String) {
        disposable = ApiClient.getInstance(this).getContact(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            if (result.id.isNullOrEmpty()) {
                                Toast.makeText(this,
                                        getString(R.string.not_found_error_description, "Contact"),
                                        Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                mItem = result
                                progressbar.visibility = View.GONE
                                setupTabs()
                            }
                        },
                        { error ->
                            progressbar.visibility = View.GONE
                            Log.e(logTag, error.message)
                            val simpleAlert = AlertDialog.Builder(this).create()
                            simpleAlert.simple(R.string.server_error_title, R.string.server_error_description)
                        }
                )
    }

    override fun setupTabs() {
        supportActionBar?.title = "Contact: " + mItem?.id.orEmpty()

        tabs.addTab(tabs.newTab().setText(getString(R.string.info)))
        tabs.addTab(tabs.newTab().setText(getString(R.string.notes)))

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager, tabs.tabCount)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
        tabs.tabMode = TabLayout.MODE_SCROLLABLE
    }

    enum class Tabs {
        Info,
        Notes
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: androidx.fragment.app.FragmentManager, private val tabCount: Int) : androidx.fragment.app.FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): androidx.fragment.app.Fragment {
            return when (values()[position]) {
                Info -> InfoListFragment.newInstance(mItem!!.getInfos())
                Notes -> NoteListFragment.newInstance(mItem!!.id.orEmpty(), ObjectType.Contact)
            }
        }

        override fun getCount(): Int {
            return tabCount
        }
    }
}
