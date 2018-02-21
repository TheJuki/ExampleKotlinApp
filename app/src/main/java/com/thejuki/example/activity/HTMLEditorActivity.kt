package com.thejuki.example.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.thejuki.example.R
import jp.wasabeef.richeditor.RichEditor
import kotlinx.android.synthetic.main.activity_htmleditor.*

/**
 * HTML Editor Activity
 *
 * Provides a standalone HTML editor activity.
 * Returns the text in HTML format back to the activity that started it for result.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class HTMLEditorActivity : BaseActivity() {

    private var mEditor: RichEditor? = null
    private var mText: String? = null
    private var mTag: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_htmleditor)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mEditor = editor

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(HTMLEditorActivity.ARG_ITEM_HTML)) {
                mText = savedInstanceState.getString(HTMLEditorActivity.ARG_ITEM_HTML)
            }
            if (savedInstanceState.containsKey(HTMLEditorActivity.ARG_ITEM_HTML)) {
                mTag = savedInstanceState.getInt(HTMLEditorActivity.ARG_ITEM_TAG)
            }
            if (savedInstanceState.containsKey(HTMLEditorActivity.ARG_ITEM_TITLE)) {
                supportActionBar?.title = savedInstanceState.getString(HTMLEditorActivity.ARG_ITEM_TITLE)
            }
        } else {
            val b = intent.extras
            if (b != null) {
                mText = b.getString(HTMLEditorActivity.ARG_ITEM_HTML)
                mTag = b.getInt(HTMLEditorActivity.ARG_ITEM_TAG)
                supportActionBar?.title = b.getString(HTMLEditorActivity.ARG_ITEM_TITLE)
            }
        }

        mEditor!!.setEditorHeight(200)
        mEditor!!.setEditorFontSize(18)
        mEditor!!.setEditorFontColor(Color.BLACK)
        mEditor!!.setPadding(10, 10, 10, 10)
        mEditor!!.setPlaceholder("")
        mEditor!!.html = mText

        mEditor!!.setOnTextChangeListener {
            mText = mEditor!!.html
        }

        action_undo.setOnClickListener { mEditor!!.undo() }

        action_redo.setOnClickListener { mEditor!!.redo() }

        action_bold.setOnClickListener { mEditor!!.setBold() }

        action_italic.setOnClickListener { mEditor!!.setItalic() }

        action_subscript.setOnClickListener { mEditor!!.setSubscript() }

        action_superscript.setOnClickListener { mEditor!!.setSuperscript() }

        action_strikethrough.setOnClickListener { mEditor!!.setStrikeThrough() }

        action_underline.setOnClickListener { mEditor!!.setUnderline() }

        action_heading1.setOnClickListener { mEditor!!.setHeading(1) }

        action_heading2.setOnClickListener { mEditor!!.setHeading(2) }

        action_heading3.setOnClickListener { mEditor!!.setHeading(3) }

        action_heading4.setOnClickListener { mEditor!!.setHeading(4) }

        action_heading5.setOnClickListener { mEditor!!.setHeading(5) }

        action_heading6.setOnClickListener { mEditor!!.setHeading(6) }

        action_txt_color.setOnClickListener(object : View.OnClickListener {
            private var isChanged: Boolean = false

            override fun onClick(v: View) {
                mEditor!!.setTextColor(if (isChanged) Color.BLACK else Color.RED)
                isChanged = !isChanged
            }
        })

        action_bg_color.setOnClickListener(object : View.OnClickListener {
            private var isChanged: Boolean = false

            override fun onClick(v: View) {
                mEditor!!.setTextBackgroundColor(if (isChanged) Color.WHITE else Color.YELLOW)
                isChanged = !isChanged
            }
        })

        action_indent.setOnClickListener { mEditor!!.setIndent() }

        action_outdent.setOnClickListener { mEditor!!.setOutdent() }

        action_align_left.setOnClickListener { mEditor!!.setAlignLeft() }

        action_align_center.setOnClickListener { mEditor!!.setAlignCenter() }

        action_align_right.setOnClickListener { mEditor!!.setAlignRight() }

        action_blockquote.setOnClickListener { mEditor!!.setBlockquote() }

        action_insert_bullets.setOnClickListener { mEditor!!.setBullets() }

        action_insert_numbers.setOnClickListener { mEditor!!.setNumbers() }

        action_insert_checkbox.setOnClickListener { mEditor!!.insertTodo() }

        mEditor!!.focusEditor()
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> {
                    hideSoftKeyBoard()
                    val intent = Intent()
                    intent.putExtra("item_value", mText.orEmpty())
                    intent.putExtra("item_tag", mTag ?: 0)
                    setResult(RESULT_FIRST_USER, intent)
                    finish()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    override fun onBackPressed() {
        // Do nothing
    }

    private fun hideSoftKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (imm.isAcceptingText) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(HTMLEditorActivity.ARG_ITEM_HTML, mText.orEmpty())
        outState.putInt(HTMLEditorActivity.ARG_ITEM_TAG, mTag ?: 0)
        outState.putString(HTMLEditorActivity.ARG_ITEM_TITLE, supportActionBar?.title.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        web_container.removeAllViews();
        mEditor!!.destroy()
    }

    companion object {
        const val ARG_ITEM_HTML = "item_html"
        const val ARG_ITEM_TITLE = "item_title"
        const val ARG_ITEM_TAG = "item_tag"
    }
}
