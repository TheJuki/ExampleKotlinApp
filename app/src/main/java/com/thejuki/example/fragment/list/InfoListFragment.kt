package com.thejuki.example.fragment.list

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thejuki.example.R
import com.thejuki.example.activity.detail.BaseDetailActivity
import com.thejuki.example.activity.detail.ContactDetailActivity
import com.thejuki.example.adapter.view.InfoItemRecyclerViewAdapter
import com.thejuki.example.enum.ObjectType
import com.thejuki.example.extension.simple
import com.thejuki.example.extension.toSafeLong
import com.thejuki.example.json.InfoJson

/**
 * Info List Fragment
 *
 * List of info items fragment
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class InfoListFragment : Fragment() {

    private var mValues: ArrayList<InfoJson>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.info_item_list, container, false)

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(ARG_INFOS)) {
                mValues = savedInstanceState.getParcelableArrayList(ARG_INFOS)
            }
        } else {
            if (arguments != null) {
                mValues = arguments!!.getParcelableArrayList(ARG_INFOS)
            }
        }

        if (view is RecyclerView) {
            val context = view.getContext()
            view.layoutManager = LinearLayoutManager(context)
            val adapter = InfoItemRecyclerViewAdapter(mValues!!.toList())

            adapter.mOnClickListener = View.OnClickListener { v ->
                val item = v.tag as InfoJson

                if (!item.id.isNullOrBlank()) {
                    when (item.objectType) {
                        ObjectType.Contact -> {
                            val intent = Intent(v.context, ContactDetailActivity::class.java).apply {
                                putExtra(BaseDetailActivity.ARG_ITEM_ID, item.id.toSafeLong())
                            }
                            v.context.startActivity(intent)
                        }
                        ObjectType.Phone -> {
                            val value = item.id.orEmpty()
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Copied Text", value)
                            clipboard.primaryClip = clip

                            val simpleAlert = AlertDialog.Builder(context).create()
                            simpleAlert.simple(R.string.phone_copied_title, value)
                        }
                        ObjectType.Email -> {
                            val value = item.id.orEmpty()
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Copied Text", value)
                            clipboard.primaryClip = clip

                            val simpleAlert = AlertDialog.Builder(context).create()
                            simpleAlert.simple(R.string.email_copied_title, value)
                        }
                        else -> {
                            // Do nothing
                        }
                    }
                }

            }
            view.adapter = adapter
        }
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(ARG_INFOS, mValues)
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val ARG_INFOS = "infoList"

        fun newInstance(infoList: java.util.ArrayList<InfoJson>): InfoListFragment {
            return InfoListFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_INFOS, infoList)
                }
            }
        }
    }
}
