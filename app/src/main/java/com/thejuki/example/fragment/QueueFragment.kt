package com.thejuki.example.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.thejuki.example.R
import com.thejuki.example.databinding.TabbedFragmentBinding
import com.thejuki.example.fragment.QueueFragment.Tabs.Counts
import com.thejuki.example.fragment.QueueFragment.Tabs.values

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
    private var _binding: TabbedFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = TabbedFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        setupTabs()
        return view
    }

    private fun setupTabs() {
        binding.tabs.addTab(binding.tabs.newTab().setText(getString(R.string.counts)))

        mSectionsPagerAdapter = SectionsPagerAdapter(childFragmentManager, binding.tabs.tabCount)

        // Set up the ViewPager with the sections adapter.
        binding.container.adapter = mSectionsPagerAdapter

        binding.container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabs))
        binding.tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(binding.container))
        binding.tabs.tabMode = TabLayout.MODE_SCROLLABLE
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
