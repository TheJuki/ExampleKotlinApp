package com.thejuki.example.adapter.array

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.thejuki.example.api.ApiClient
import com.thejuki.example.json.ContactLookupJson

/**
 * Contact AutoComplete Adapter
 *
 * AutoComplete Adapter used for Contact fields.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class ContactAutoCompleteAdapter(context: Context, textViewResourceId: Int, val defaultItems: ArrayList<ContactLookupJson> = ArrayList()) :
        ArrayAdapter<ContactLookupJson>(context, textViewResourceId), Filterable {

    internal var items: ArrayList<ContactLookupJson>

    init {
        items = defaultItems
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(index: Int): ContactLookupJson? {
        return items[index]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var mConvertView = convertView
        if (convertView == null) {
            val inflater = context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            mConvertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
        }
        (mConvertView!!.findViewById(android.R.id.text1) as TextView).text = getItem(position)!!.label
        return mConvertView
    }

    override fun getFilter(): Filter {

        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                items = if (constraint != null && constraint.length > 2) {
                    ApiClient.getInstance(context).getAutoCompleteContacts(constraint.toString())

                } else {
                    this@ContactAutoCompleteAdapter.defaultItems
                }

                filterResults.values = items
                filterResults.count = items.size

                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }

        }

    }
}