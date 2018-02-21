package com.thejuki.example.fragment.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thejuki.example.activity.detail.BaseDetailActivity
import com.thejuki.example.activity.detail.ContactDetailActivity
import com.thejuki.example.activity.form.ContactFormActivity
import com.thejuki.example.api.ApiClient
import com.thejuki.example.api.AuthManager
import com.thejuki.example.json.TableCellJson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Contact List Fragment
 *
 * List of contacts fragment
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class ContactListFragment : BaseListFragment() {
    private val logTag = "ContactList"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        if (AuthManager.getInstance(context!!).has("contact_add")) {
            mFab!!.setOnClickListener { _ ->
                val intent = Intent(activity, ContactFormActivity::class.java)
                startActivity(intent)
            }
        } else {
            mFab!!.hide()
        }

        mItemRecyclerViewAdapter!!.mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as TableCellJson
            val intent = Intent(v.context, ContactDetailActivity::class.java).apply {
                putExtra(BaseDetailActivity.ARG_ITEM_ID, item.id!!)
            }
            v.context.startActivity(intent)
        }

        mHeader!!.visibility = View.GONE

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        reload()
    }

    override fun reload() {
        if (isSearching) {
            disposable = ApiClient.getInstance(context!!).getContacts(searchTerm.orEmpty())
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
        } else {
            showResults(mutableListOf(), false)
        }
    }

    companion object {
        fun newInstance(searchTerm: String? = null,
                        isSearching: Boolean = false): ContactListFragment {
            return ContactListFragment().apply {
                arguments = BaseListFragment.getBundle(
                        searchTerm,
                        isSearching)
            }
        }
    }
}
