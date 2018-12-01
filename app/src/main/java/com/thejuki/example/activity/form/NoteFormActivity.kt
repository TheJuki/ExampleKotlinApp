package com.thejuki.example.activity.form

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Toast
import com.thejuki.example.R
import com.thejuki.example.activity.HTMLEditorActivity
import com.thejuki.example.activity.detail.BaseDetailActivity
import com.thejuki.example.activity.detail.NoteDetailActivity
import com.thejuki.example.activity.form.NoteFormActivity.Tag.*
import com.thejuki.example.api.ApiClient
import com.thejuki.example.extension.simple
import com.thejuki.example.json.ListItemJson
import com.thejuki.example.json.NoteJson
import com.thejuki.kformmaster.helper.*
import com.thejuki.kformmaster.listener.OnFormElementValueChangedListener
import com.thejuki.kformmaster.model.BaseFormElement
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_form.*

/**
 * Note Form Activity
 *
 * Uses the KFormMaster library to add/edit a Note.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class NoteFormActivity : BaseFormActivity<NoteJson>() {
    private val logTag = "NoteForm"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (mItem == null) {
            mItem = NoteJson(formAction = "Add")
        } else {
            if (mItem!!.id.isNullOrEmpty()) {
                mItem!!.formAction = "Add"
            } else {
                mItem!!.formAction = "Edit"
            }
        }

        if (mItem!!.formAction == "Add") {
            supportActionBar?.title = "New Note"
            mItem!!.type = "Status"
        } else {
            supportActionBar?.title = "Note: " + mItem!!.id.orEmpty()
        }

        setupForm()
    }

    private enum class Tag {
        Type,
        Body
    }

    override fun setupForm() {
        val listener = object : OnFormElementValueChangedListener {
            override fun onValueChanged(formElement: BaseFormElement<*>) {
                when (values()[formElement.tag]) {
                    Type -> {
                        val type = formElement.value as ListItemJson?
                        mItem!!.type = type?.name
                    }
                    Body -> {
                        val intent = Intent(this@NoteFormActivity,
                                HTMLEditorActivity::class.java).apply {
                            putExtra(HTMLEditorActivity.ARG_ITEM_HTML, mItem!!.body)
                            putExtra(HTMLEditorActivity.ARG_ITEM_TITLE, getString(R.string.lbl_body))
                            putExtra(HTMLEditorActivity.ARG_ITEM_TAG, Body.ordinal)
                        }
                        startActivityForResult(intent, 0)
                    }
                }
            }
        }

        form(this, formRecyclerView, listener) {
            if (mItem!!.formAction == "Add") {
                dropDown<ListItemJson>(Type.ordinal) {
                    title = getString(R.string.lbl_type)
                    dialogTitle = getString(R.string.lbl_type_select)
                    options = arrayListOf(
                            ListItemJson(id = "1", name = "Status"),
                            ListItemJson(id = "2", name = "Log"))
                    value = options!!.find { item -> item.name == "Status" }
                }
            } else {
                textView(Type.ordinal)
                {
                    title = getString(R.string.lbl_type)
                    value = mItem!!.type.orEmpty()
                }
            }
            header { title = getString(R.string.lbl_body) }
            button(Body.ordinal)
            {
                value = getString(R.string.lbl_edit_body)
            }
        }
    }

    override fun save() {
        showProgressBar()

        if (validateForm()) {

            val simpleAlert = AlertDialog.Builder(this).create()

            disposable = ApiClient.getInstance(this).postNote(mItem!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { result ->
                                if (!result.id.isNullOrEmpty()) {
                                    Toast.makeText(this, "Note saved",
                                            Toast.LENGTH_SHORT)
                                            .show()

                                    if (mItem!!.formAction == "Add") {
                                        val intent = Intent(this, NoteDetailActivity::class.java).apply {
                                            putExtra(BaseDetailActivity.ARG_ITEM_ID, result.id!!)
                                        }
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        val intent = Intent(this, NoteDetailActivity::class.java).apply {
                                            putExtra(BaseDetailActivity.ARG_ITEM_ID, mItem!!.id!!)
                                        }
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        startActivity(intent)
                                        finish()
                                    }
                                } else {
                                    hideProgressBar()
                                    simpleAlert.simple(R.string.verification_error_title, R.string.verification_error_description)
                                }
                            },
                            { error ->
                                hideProgressBar()
                                Log.e(logTag, error.message)
                                simpleAlert.simple(R.string.form_error_title, R.string.form_error_description)
                            }
                    )
        } else {
            hideProgressBar()
        }
    }

    override fun validateForm(): Boolean {
        val simpleAlert = AlertDialog.Builder(this).create()
        var isValid = true

        if (isValid && mItem!!.body.isNullOrBlank()) {
            simpleAlert.simple(R.string.needed_body_error_title, R.string.needed_body_error_description)
            isValid = false
        }
        if (mItem!!.formAction.orEmpty() == "Add") {

            if (isValid && mItem!!.type.isNullOrBlank()) {
                simpleAlert.simple(R.string.needed_type_error_title, R.string.needed_type_error_description)
                isValid = false;
            }
        }

        return isValid
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != 0 || data == null) {
            return
        }

        val tag: Int = data.getIntExtra("item_tag", 0)
        val value: String = data.getStringExtra("item_value")

        when (values()[tag]) {
            Body -> {
                mItem!!.body = value
            }
            else -> {
            }
        }
    }
}
