package com.thejuki.example.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thejuki.example.PreferenceConstants
import com.thejuki.example.R
import com.thejuki.example.extension.PreferenceHelper

/**
 * Base Activity
 *
 * Handles setting the dark theme, if enabled, to override the AndroidManifest activity theme.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val defPrefs = PreferenceHelper.defaultPrefs(this)
        if (defPrefs.getBoolean(PreferenceConstants.theme, false)) {
            if (packageManager.getActivityInfo(componentName, 0).theme == R.style.AppTheme_NoActionBar) {
                setTheme(R.style.AppTheme_Dark_NoActionBar)
            } else {
                setTheme(R.style.AppTheme_Dark)
            }
        }
    }
}