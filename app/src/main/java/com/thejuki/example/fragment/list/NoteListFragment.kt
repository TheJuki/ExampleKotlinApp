package com.thejuki.example.fragment.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.thejuki.example.R
import com.thejuki.example.activity.detail.BaseDetailActivity
import com.thejuki.example.activity.detail.NoteDetailActivity
import com.thejuki.example.api.ApiClient
import com.thejuki.example.enum.NoteType
import com.thejuki.example.enum.ObjectType
import com.thejuki.example.json.TableCellJson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Note List Fragment
 *
 * List of notes fragment
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class NoteListFragment : BaseListFragment() {
    private val logTag = "NoteList"
    private var type = NoteType.All
    private var parentId = ""
    private var parentType = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(ARG_TYPE)) {
                type = savedInstanceState.getSerializable(ARG_TYPE) as NoteType
            }
            if (savedInstanceState.containsKey(ARG_PARENT_ID)) {
                parentId = savedInstanceState.getString(ARG_PARENT_ID) ?: ""
            }
            if (savedInstanceState.containsKey(ARG_PARENT_TYPE)) {
                parentType = savedInstanceState.getString(ARG_PARENT_TYPE) ?: ""
            }
        } else {
            if (arguments != null) {
                with(arguments!!)
                {
                    type = this.getSerializable(ARG_TYPE) as NoteType
                    parentId = this.getString(ARG_PARENT_ID) ?: ""
                    parentType = this.getString(ARG_PARENT_TYPE) ?: ""
                }

            }
        }

        mFab!!.hide()

        mItemRecyclerViewAdapter!!.mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as TableCellJson
            val intent = Intent(v.context, NoteDetailActivity::class.java).apply {
                putExtra(BaseDetailActivity.ARG_ITEM_ID, item.id!!)
            }
            v.context.startActivity(intent)
        }

        mHeader!!.setOnClickListener { _ ->
            val options = NoteType.values().map { it.value }.toTypedArray()
            AlertDialog.Builder(context!!)
                    .setTitle(getString(R.string.filter))
                    .setSingleChoiceItems(options, type.ordinal) { d, which ->
                        type = NoteType.values()[which]
                        d.dismiss()
                        showLoading()
                        reload()
                    }
                    .setNegativeButton(getString(android.R.string.cancel)) { _, _ ->
                    }
                    .create()
                    .show()
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        reload()
    }

    override fun reload() {
        disposable = ApiClient.getInstance(context!!).getNotes(parentId, parentType,
                type.value)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            showResults(result)
                        },
                        { error ->
                            Log.e(logTag, error.message)
                            showError()
                        }
                )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(ARG_TYPE, type)
        outState.putString(ARG_PARENT_ID, parentId)
        outState.putString(ARG_PARENT_TYPE, parentType)
    }

    companion object {
        const val ARG_TYPE = "type"
        const val ARG_PARENT_ID = "parentId"
        const val ARG_PARENT_TYPE = "parentType"

        fun newInstance(parentId: String,
                        parentObjectType: ObjectType,
                        type: NoteType? = NoteType.All): NoteListFragment {
            return NoteListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARENT_ID, parentId)
                    putString(ARG_PARENT_TYPE, parentObjectType.value)
                    putSerializable(ARG_TYPE, type)
                }
            }
        }
    }
}
