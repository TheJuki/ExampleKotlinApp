package com.thejuki.example.adapter.view

import android.content.SharedPreferences
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.thejuki.example.PreferenceConstants
import com.thejuki.example.R
import com.thejuki.example.extension.PreferenceHelper
import com.thejuki.example.json.InfoJson

import kotlinx.android.synthetic.main.info_item_list_content.view.*

/**
 * Info Item Recycler View Adapter
 *
 * Recycler View Adapter used for Info tab.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class InfoItemRecyclerViewAdapter(private val mValues: List<InfoJson>) : RecyclerView.Adapter<InfoItemRecyclerViewAdapter.ViewHolder>() {

    lateinit var mOnClickListener: View.OnClickListener
    lateinit var prefs: SharedPreferences

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.info_item_list_content, parent, false)
        prefs = PreferenceHelper.defaultPrefs(parent.context)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        val fontSize: Float = prefs.getString(PreferenceConstants.fontSize, "16")?.toFloatOrNull()
                ?: 16f
        holder.mTitle.text = item.title.orEmpty()
        holder.mTitle.textSize = fontSize
        holder.mSubtitle.text = item.content.orEmpty()
        holder.mSubtitle.textSize = fontSize

        with(holder.itemView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val mTitle: TextView = mView.title
        val mSubtitle: TextView = mView.subtitle
    }
}
