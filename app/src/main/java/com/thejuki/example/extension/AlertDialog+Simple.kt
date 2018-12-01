package com.thejuki.example.extension

import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog

/**
 * AlertDialog Extensions
 *
 * Simplifies simple "OK" dialogs that do not require a button action.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
fun AlertDialog.simple(@StringRes titleId: Int, @StringRes messageId: Int) {
    this.setTitle(this.context.getString(titleId))
    this.setMessage(this.context.getString(messageId))
    this.setButton(AlertDialog.BUTTON_POSITIVE, this.context.getString(android.R.string.ok)) { _, _ ->
    }
    this.show()
}

fun AlertDialog.simple(@StringRes titleId: Int, message: String) {
    this.setTitle(this.context.getString(titleId))
    this.setMessage(message)
    this.setButton(AlertDialog.BUTTON_POSITIVE, this.context.getString(android.R.string.ok)) { _, _ ->
    }
    this.show()
}
