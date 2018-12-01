package com.thejuki.example.adapter.view

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.thejuki.example.PreferenceConstants
import com.thejuki.example.R
import com.thejuki.example.extension.PreferenceHelper
import com.thejuki.example.json.TableCellJson
import kotlinx.android.synthetic.main.item_list_content.view.*

/**
 * Item Recycler View Adapter
 *
 * Recycler View Adapter used for fragment lists.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class ItemRecyclerViewAdapter(val mValues: MutableList<TableCellJson> = mutableListOf<TableCellJson>()) :
        androidx.recyclerview.widget.RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder>() {

    lateinit var mOnClickListener: View.OnClickListener
    lateinit var prefs: SharedPreferences

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        val fontSize: Float = prefs.getString(PreferenceConstants.fontSize, "16")?.toFloatOrNull()
                ?: 16f
        holder.mId.text = String.format("#%s", item.id.orEmpty())
        holder.mId.textSize = fontSize + 2
        holder.mAccount.text = item.account.orEmpty()
        holder.mAccount.textSize = fontSize
        holder.mCategory.text = item.category.orEmpty()
        holder.mCategory.textSize = fontSize
        holder.mContact.text = item.contact.orEmpty()
        holder.mContact.textSize = fontSize
        holder.mDate.text = item.date.orEmpty()
        holder.mDate.textSize = fontSize
        holder.mStatus.text = item.status ?: "N/A"
        holder.mStatus.textSize = fontSize

        with(holder.itemView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_content, parent, false)
        prefs = PreferenceHelper.defaultPrefs(parent.context)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(mView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(mView) {
        val mId: TextView = mView.item_id
        val mAccount: TextView = mView.item_account
        val mCategory: TextView = mView.item_category
        val mStatus: TextView = mView.item_status
        val mContact: TextView = mView.item_contact
        val mDate: TextView = mView.item_date
    }
}
