package com.thejuki.example.activity.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.thejuki.example.R
import com.thejuki.example.activity.detail.NoteDetailActivity.Tabs.*
import com.thejuki.example.activity.form.BaseFormActivity
import com.thejuki.example.activity.form.NoteFormActivity
import com.thejuki.example.api.ApiClient
import com.thejuki.example.api.AuthManager
import com.thejuki.example.databinding.SheetNoteBinding
import com.thejuki.example.extension.simple
import com.thejuki.example.fragment.ItemInfoFragment
import com.thejuki.example.fragment.list.InfoListFragment
import com.thejuki.example.json.NoteJson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Note Detail Activity
 *
 * Note Details
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class NoteDetailActivity : BaseDetailActivity<NoteJson>() {
    private val logTag = "NoteDetail"
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private lateinit var bottomSheetBinding: SheetNoteBinding

    @SuppressLint("InflateParams")
    override fun setupBottomSheet() {
        mBottomSheetDialog = BottomSheetDialog(this)
        bottomSheetBinding = SheetNoteBinding.inflate(layoutInflater)
        val view = bottomSheetBinding.root
        mBottomSheetDialog.setContentView(view)

        bottomSheetBinding.sheetClose.setOnClickListener {
            mBottomSheetDialog.dismiss()
        }

        if (AuthManager.getInstance(this).has("note_edit")) {
            bottomSheetBinding.sheetEdit.setOnClickListener {
                mBottomSheetDialog.dismiss()
                val intent = Intent(this, NoteFormActivity::class.java).apply {
                    putExtra(BaseFormActivity.ARG_ITEM, mItem)
                }
                startActivity(intent)
            }
        } else {
            bottomSheetBinding.sheetEdit.visibility = View.GONE
        }
    }

    override fun getItem(id: String) {
        disposable = ApiClient.getInstance(this).getNote(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            if (result.id.isNullOrEmpty()) {
                                Toast.makeText(this,
                                        getString(R.string.not_found_error_description, "Note"),
                                        Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                mItem = result
                                binding.progressbar.visibility = View.GONE
                                if (mItem!!.modifyingUser != ApiClient.getInstance(this).getUsername()) {
                                    bottomSheetBinding.sheetEdit.visibility = View.GONE
                                }
                                setupTabs()
                            }
                        },
                        { error ->
                            binding.progressbar.visibility = View.GONE
                            Log.e(logTag, error.message ?: "")
                            val simpleAlert = AlertDialog.Builder(this).create()
                            simpleAlert.simple(R.string.server_error_title, R.string.server_error_description)
                        }
                )
    }

    override fun setupTabs() {
        supportActionBar?.title = "Note: " + mItem?.id.orEmpty()

        binding.tabs.addTab(binding.tabs.newTab().setText(getString(R.string.info)))
        binding.tabs.addTab(binding.tabs.newTab().setText(getString(R.string.lbl_body)))

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager, binding.tabs.tabCount)

        // Set up the ViewPager with the sections adapter.
        binding.container.adapter = mSectionsPagerAdapter

        binding.container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabs))
        binding.tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(binding.container))
        binding.tabs.tabMode = TabLayout.MODE_SCROLLABLE
    }

    enum class Tabs {
        Info,
        Body
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: androidx.fragment.app.FragmentManager, private val tabCount: Int) : androidx.fragment.app.FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): androidx.fragment.app.Fragment {
            return when (values()[position]) {
                Info -> InfoListFragment.newInstance(mItem!!.getInfos())
                Body -> ItemInfoFragment.newInstance(mItem!!.body.orEmpty())
            }
        }

        override fun getCount(): Int {
            return tabCount
        }
    }
}
