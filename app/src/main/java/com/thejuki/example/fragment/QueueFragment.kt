package com.thejuki.example.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.thejuki.example.R
import com.thejuki.example.fragment.QueueFragment.Tabs.Counts
import com.thejuki.example.fragment.QueueFragment.Tabs.values
import kotlinx.android.synthetic.main.tabbed_fragment.view.*

/**
 * Queue Fragment
 *
 * An example for using tabs in a fragment and using Charts that can be refreshed
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class QueueFragment : androidx.fragment.app.Fragment() {
    private var mSectionsPagerAdapter: QueueFragment.SectionsPagerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tabbed_fragment, container, false)
        setupTabs(view)
        return view
    }

    private fun setupTabs(view: View) {
        view.tabs.addTab(view.tabs.newTab().setText(getString(R.string.counts)))

        mSectionsPagerAdapter = SectionsPagerAdapter(childFragmentManager, view.tabs.tabCount)

        // Set up the ViewPager with the sections adapter.
        view.container.adapter = mSectionsPagerAdapter

        view.container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(view.tabs))
        view.tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(view.container))
        view.tabs.tabMode = TabLayout.MODE_SCROLLABLE
    }

    enum class Tabs {
        Counts
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: androidx.fragment.app.FragmentManager, private val tabCount: Int) : androidx.fragment.app.FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): androidx.fragment.app.Fragment {
            return when (values()[position]) {
                Counts -> ChartFragment.newInstance()
            }
        }

        override fun getCount(): Int {
            return tabCount
        }
    }

    companion object {
        fun newInstance(): QueueFragment {
            return QueueFragment()
        }
    }
}
