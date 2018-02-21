package com.thejuki.example.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle

/**
 * Splash Activity
 *
 * Shows the drawable splash screen when the app opens.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, MainActivity::class.java))

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}
