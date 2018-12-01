package com.thejuki.example.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.thejuki.example.PreferenceConstants
import com.thejuki.example.R
import com.thejuki.example.extension.PreferenceHelper
import com.thejuki.example.extension.fromHtml
import kotlinx.android.synthetic.main.fragment_item_info.view.*

/**
 * Item Info Fragment
 *
 * A fragment TextView with HTML support
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class ItemInfoFragment : androidx.fragment.app.Fragment() {

    private var infoString: String? = null
    private var mInfoTextView: TextView? = null
    lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            infoString = arguments!!.getString(ARG_INFO)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_item_info, container, false)
        prefs = PreferenceHelper.defaultPrefs(context!!)
        val fontSize: Float = prefs.getString(PreferenceConstants.fontSize, "16").toFloatOrNull()
                ?: 16f
        mInfoTextView = view.infoText
        mInfoTextView!!.text = infoString.fromHtml()
        mInfoTextView!!.textSize = fontSize

        return view
    }

    companion object {
        private const val ARG_INFO = "info"

        fun newInstance(info: String): ItemInfoFragment {
            return ItemInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_INFO, info)
                }
            }
        }
    }
}
