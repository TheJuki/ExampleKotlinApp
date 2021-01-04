package com.thejuki.example.adapter.view

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.thejuki.example.PreferenceConstants
import com.thejuki.example.R
import com.thejuki.example.databinding.ItemListContentBinding
import com.thejuki.example.extension.PreferenceHelper
import com.thejuki.example.json.TableCellJson

/**
 * Item Recycler View Adapter
 *
 * Recycler View Adapter used for fragment lists.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class ItemRecyclerViewAdapter(val mValues: MutableList<TableCellJson> = mutableListOf()) :
        androidx.recyclerview.widget.RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder>() {

    lateinit var mOnClickListener: View.OnClickListener
    lateinit var prefs: SharedPreferences
    private lateinit var binding: ItemListContentBinding

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
        binding = ItemListContentBinding.bind(view)
        prefs = PreferenceHelper.defaultPrefs(parent.context)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(mView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(mView) {
        val mId: TextView = binding.itemId
        val mAccount: TextView = binding.itemAccount
        val mCategory: TextView = binding.itemCategory
        val mStatus: TextView = binding.itemStatus
        val mContact: TextView = binding.itemContact
        val mDate: TextView = binding.itemDate
    }
}
