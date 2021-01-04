package com.thejuki.example.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.thejuki.example.databinding.ActivityHtmleditorBinding

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

    private var mText: String? = null
    private var mTag: Int? = null
    private lateinit var binding: ActivityHtmleditorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHtmleditorBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(ARG_ITEM_HTML)) {
                mText = savedInstanceState.getString(ARG_ITEM_HTML)
            }
            if (savedInstanceState.containsKey(ARG_ITEM_HTML)) {
                mTag = savedInstanceState.getInt(ARG_ITEM_TAG)
            }
            if (savedInstanceState.containsKey(ARG_ITEM_TITLE)) {
                supportActionBar?.title = savedInstanceState.getString(ARG_ITEM_TITLE)
            }
        } else {
            val b = intent.extras
            if (b != null) {
                mText = b.getString(ARG_ITEM_HTML)
                mTag = b.getInt(ARG_ITEM_TAG)
                supportActionBar?.title = b.getString(ARG_ITEM_TITLE)
            }
        }

        binding.editor.setEditorHeight(200)
        binding.editor.setEditorFontSize(18)
        binding.editor.setEditorFontColor(Color.BLACK)
        binding.editor.setPadding(10, 10, 10, 10)
        binding.editor.setPlaceholder("")
        binding.editor.html = mText

        binding.editor.setOnTextChangeListener {
            mText = binding.editor.html
        }

        binding.actionUndo.setOnClickListener { binding.editor.undo() }

        binding.actionRedo.setOnClickListener { binding.editor.redo() }

        binding.actionBold.setOnClickListener { binding.editor.setBold() }

        binding.actionItalic.setOnClickListener { binding.editor.setItalic() }

        binding.actionSubscript.setOnClickListener { binding.editor.setSubscript() }

        binding.actionSuperscript.setOnClickListener { binding.editor.setSuperscript() }

        binding.actionStrikethrough.setOnClickListener { binding.editor.setStrikeThrough() }

        binding.actionUnderline.setOnClickListener { binding.editor.setUnderline() }

        binding.actionHeading1.setOnClickListener { binding.editor.setHeading(1) }

        binding.actionHeading2.setOnClickListener { binding.editor.setHeading(2) }

        binding.actionHeading3.setOnClickListener { binding.editor.setHeading(3) }

        binding.actionHeading4.setOnClickListener { binding.editor.setHeading(4) }

        binding.actionHeading5.setOnClickListener { binding.editor.setHeading(5) }

        binding.actionHeading6.setOnClickListener { binding.editor.setHeading(6) }

        binding.actionTxtColor.setOnClickListener(object : View.OnClickListener {
            private var isChanged: Boolean = false

            override fun onClick(v: View) {
                binding.editor.setTextColor(if (isChanged) Color.BLACK else Color.RED)
                isChanged = !isChanged
            }
        })

        binding.actionBgColor.setOnClickListener(object : View.OnClickListener {
            private var isChanged: Boolean = false

            override fun onClick(v: View) {
                binding.editor.setTextBackgroundColor(if (isChanged) Color.WHITE else Color.YELLOW)
                isChanged = !isChanged
            }
        })

        binding.actionIndent.setOnClickListener { binding.editor.setIndent() }

        binding.actionOutdent.setOnClickListener { binding.editor.setOutdent() }

        binding.actionAlignLeft.setOnClickListener { binding.editor.setAlignLeft() }

        binding.actionAlignCenter.setOnClickListener { binding.editor.setAlignCenter() }

        binding.actionAlignRight.setOnClickListener { binding.editor.setAlignRight() }

        binding.actionBlockquote.setOnClickListener { binding.editor.setBlockquote() }

        binding.actionInsertBullets.setOnClickListener { binding.editor.setBullets() }

        binding.actionInsertNumbers.setOnClickListener { binding.editor.setNumbers() }

        binding.actionInsertCheckbox.setOnClickListener { binding.editor.insertTodo() }

        binding.editor.focusEditor()
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
        outState.putString(ARG_ITEM_HTML, mText.orEmpty())
        outState.putInt(ARG_ITEM_TAG, mTag ?: 0)
        outState.putString(ARG_ITEM_TITLE, supportActionBar?.title.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.webContainer.removeAllViews()
        binding.editor.destroy()
    }

    companion object {
        const val ARG_ITEM_HTML = "item_html"
        const val ARG_ITEM_TITLE = "item_title"
        const val ARG_ITEM_TAG = "item_tag"
    }
}
