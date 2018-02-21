package com.thejuki.example.adapter.array

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filterable
import android.widget.TextView
import com.thejuki.example.json.ListItemJson

import java.util.*

/**
 * ListItem Adapter
 *
 * Array Adapter used for dropdown fields.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class ListItemAdapter(context: Context, textViewResourceId: Int) :
        ArrayAdapter<ListItemJson>(context, textViewResourceId), Filterable {

    private var items: ArrayList<ListItemJson> = ArrayList()

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(index: Int): ListItemJson? {
        return items[index]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var mConvertView = convertView
        if (convertView == null) {
            val inflater = context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            mConvertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
        }
        (mConvertView!!.findViewById(android.R.id.text1) as TextView).text = getItem(position)!!.name
        return mConvertView
    }

    fun loadItems(items: ArrayList<ListItemJson>) {
        if (items.count() > 0) {
            this.items.clear()
            this.items.addAll(items)
            notifyDataSetChanged()
        } else {
            this.items.clear()
            notifyDataSetInvalidated()
        }
    }
}