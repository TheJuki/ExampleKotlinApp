package com.thejuki.example.activity

import android.content.Intent
import com.github.omadahealth.lollipin.lib.managers.AppLockActivity

/**
 * Custom Pin Lock Activity
 *
 * Handles the success, failure, and forgot dialog of AppLockActivity
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class CustomPinLockActivity : AppLockActivity() {
    override fun onPinSuccess(attempts: Int) {

    }

    override fun onPinFailure(attempts: Int) {
        if (attempts == 2) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
    }

    override fun showForgotDialog() {

    }
}