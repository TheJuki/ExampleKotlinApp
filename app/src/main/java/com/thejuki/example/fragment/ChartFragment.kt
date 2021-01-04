package com.thejuki.example.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.snackbar.Snackbar
import com.thejuki.example.R
import com.thejuki.example.api.ApiClient
import com.thejuki.example.databinding.FragmentWorkQueueChartBinding
import com.thejuki.example.extension.getThemeAttrColor
import com.thejuki.example.extension.simple
import com.thejuki.example.json.CountsJson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

/**
 * Chart Fragment
 *
 * A refreshable, API-driven Chart fragment
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class ChartFragment : androidx.fragment.app.Fragment() {
    private val logTag = "Chart"
    private var disposable: Disposable? = null
    private var _binding: FragmentWorkQueueChartBinding? = null
    private val binding get() = _binding!!

    private var mItem: CountsJson? = null
    private var mSwipeRefreshLayout: androidx.swiperefreshlayout.widget.SwipeRefreshLayout? = null
    private var mCoordinatorLayout: View? = null
    private var mFirstPieChart: PieChart? = null
    private var mSecondPieChart: PieChart? = null
    private var mThirdPieChart: PieChart? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentWorkQueueChartBinding.inflate(inflater, container, false)
        val view = binding.root

        mFirstPieChart = binding.firstPieChart
        mSecondPieChart = binding.secondPieChart
        mThirdPieChart = binding.thirdPieChart

        mSwipeRefreshLayout = binding.swiperefresh
        mCoordinatorLayout = binding.clayout

        mSwipeRefreshLayout!!.setOnRefreshListener {
            this.reload()
        }
        mSwipeRefreshLayout!!.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3)

        mSwipeRefreshLayout!!.isRefreshing = true

        reload()

        return view
    }

    private fun reload() {
        disposable = ApiClient.getInstance(context!!).getCounts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            mItem = result
                            updateFirstPieChartData()
                            updateSecondPieChartData()
                            updateThirdPieChartData()
                            mSwipeRefreshLayout!!.isRefreshing = false

                            val date = Calendar.getInstance().time
                            val formatter = SimpleDateFormat("h:mm a", Locale.US)

                            Snackbar.make(mCoordinatorLayout!!,
                                    String.format(getString(R.string.list_updated_general), formatter.format(date)),
                                    Snackbar.LENGTH_LONG).show()
                        },
                        { error ->
                            mSwipeRefreshLayout!!.isRefreshing = false
                            Log.e(logTag, error.message ?: "")
                            val simpleAlert = AlertDialog.Builder(context!!).create()
                            simpleAlert.simple(R.string.server_error_title, R.string.server_error_description)
                        }
                )
    }

    private fun updateFirstPieChartData() {
        val chartEntries: MutableList<PieEntry> = mutableListOf()
        val pieColors: MutableList<Int> = mutableListOf()

        var entry = mItem?.entry1 ?: 0
        if (entry > 0) {
            chartEntries.add(PieEntry(entry.toFloat(), getString(R.string.greaterThan4Hours)))
            pieColors.add(ContextCompat.getColor(context!!, R.color.pie_slice_red))
        }
        entry = mItem?.entry2 ?: 0
        if (entry > 0) {
            chartEntries.add(PieEntry(entry.toFloat(), getString(R.string.greaterThan2Hours)))
            pieColors.add(ContextCompat.getColor(context!!, R.color.pie_slice_orange))
        }
        entry = mItem?.entry3 ?: 0
        if (entry > 0) {
            chartEntries.add(PieEntry(entry.toFloat(), getString(R.string.greaterThan30Minutes)))
            pieColors.add(ContextCompat.getColor(context!!, R.color.pie_slice_yellow))
        }
        entry = mItem?.entry4 ?: 0

        if (entry > 0) {
            chartEntries.add(PieEntry(entry.toFloat(), getString(R.string.lessThan30Minutes)))
            pieColors.add(ContextCompat.getColor(context!!, R.color.pie_slice_blue))
        }

        if (chartEntries.size == 0) {
            chartEntries.add(PieEntry(0.1f, ""))
            pieColors.add(ContextCompat.getColor(context!!, R.color.pie_slice_green))
        }

        val dataSet = PieDataSet(chartEntries, "")
        dataSet.colors = pieColors

        updatePieChart(mFirstPieChart!!, dataSet, getString(R.string.firstChartTitle))
    }

    private fun updateSecondPieChartData() {
        val chartEntries: MutableList<PieEntry> = mutableListOf()
        val pieColors: MutableList<Int> = mutableListOf()

        var entry = mItem?.entry5 ?: 0
        if (entry > 0) {
            chartEntries.add(PieEntry(entry.toFloat(), getString(R.string.highPriority)))
            pieColors.add(ContextCompat.getColor(context!!, R.color.pie_slice_red))
        }
        entry = mItem?.entry6 ?: 0
        if (entry > 0) {
            chartEntries.add(PieEntry(entry.toFloat(), getString(R.string.lowPriority)))
            pieColors.add(ContextCompat.getColor(context!!, R.color.pie_slice_blue))
        }

        if (chartEntries.size == 0) {
            chartEntries.add(PieEntry(0.1f, ""))
            pieColors.add(ContextCompat.getColor(context!!, R.color.pie_slice_green))
        }

        val dataSet = PieDataSet(chartEntries, "")
        dataSet.colors = pieColors

        updatePieChart(mSecondPieChart!!, dataSet, getString(R.string.secondChartTitle))
    }

    private fun updateThirdPieChartData() {
        val chartEntries: MutableList<PieEntry> = mutableListOf()
        val pieColors: MutableList<Int> = mutableListOf()

        val entry = mItem?.entry7 ?: 0
        if (entry > 0) {
            chartEntries.add(PieEntry(entry.toFloat(), ""))
            pieColors.add(ContextCompat.getColor(context!!, R.color.pie_slice_blue))
        }

        if (chartEntries.size == 0) {
            chartEntries.add(PieEntry(0.1f, ""))
            pieColors.add(ContextCompat.getColor(context!!, R.color.pie_slice_green))
        }

        val dataSet = PieDataSet(chartEntries, "")
        dataSet.colors = pieColors

        updatePieChart(mThirdPieChart!!, dataSet, getString(R.string.thirdChartTitle))
    }

    private fun updatePieChart(pieChart: PieChart, dataSet: PieDataSet, centerText: String) {

        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 2.0f
        dataSet.valueTextSize = 20f
        dataSet.valueTextColor = Color.WHITE
        dataSet.iconsOffset = MPPointF(0f, 40f)
        val formatter = DefaultValueFormatter(0)
        dataSet.valueFormatter = formatter

        pieChart.data = PieData(dataSet)
        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.legend.isEnabled = false
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad)
        pieChart.centerText = centerText
        pieChart.setCenterTextColor(R.attr.colorAccent.getThemeAttrColor(context!!))
        pieChart.setCenterTextSize(20f)
        pieChart.setHoleColor(R.attr.listBackground.getThemeAttrColor(context!!))
        pieChart.isDrawHoleEnabled = true
        pieChart.rotationAngle = 0.0f
        pieChart.isRotationEnabled = false
        pieChart.isHighlightPerTapEnabled = true
        pieChart.description.isEnabled = false

        pieChart.notifyDataSetChanged()
        pieChart.invalidate()
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    companion object {
        fun newInstance(): ChartFragment {
            return ChartFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
