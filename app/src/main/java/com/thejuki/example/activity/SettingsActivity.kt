package com.thejuki.example.activity

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceActivity
import android.preference.PreferenceFragment
import android.view.MenuItem
import com.github.omadahealth.lollipin.lib.managers.AppLock
import com.github.omadahealth.lollipin.lib.managers.LockManager
import com.thejuki.example.PreferenceConstants
import com.thejuki.example.R
import com.thejuki.example.extension.PreferenceHelper

/**
 * Settings Activity
 *
 * Implementation of AppCompatPreferenceActivity to provide an activity to change user settings.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class SettingsActivity : AppCompatPreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupActionBar()
        supportActionBar!!.show()
    }

    /**
     * Set up the [android.app.ActionBar], if the API is available.
     */
    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            if (isTaskRoot) {
                finish()
                startActivity(Intent(this, DrawerActivity::class.java))
                return true
            } else {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    finish()
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (isTaskRoot) {
            finish()
            startActivity(Intent(this, DrawerActivity::class.java))
        } else {
            super.onBackPressed()
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun onIsMultiPane(): Boolean {
        return isXLargeTablet(this)
    }

    /**
     * {@inheritDoc}
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    override fun onBuildHeaders(target: List<PreferenceActivity.Header>) {
        loadHeadersFromResource(R.xml.pref_headers, target)
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    override fun isValidFragment(fragmentName: String): Boolean {
        return PreferenceFragment::class.java.name == fragmentName
                || AboutPreferenceFragment::class.java.name == fragmentName
                || GeneralPreferenceFragment::class.java.name == fragmentName
                || SecurityPreferenceFragment::class.java.name == fragmentName
    }

    /**
     * General Settings
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    class GeneralPreferenceFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_general)
            setHasOptionsMenu(true)
            bindPreferenceSummaryToValue(findPreference(PreferenceConstants.fontSize))

            findPreference(PreferenceConstants.theme).onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, _ ->
                activity.finish()
                val intent = activity.intent
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                true
            }
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            val id = item.itemId
            if (id == android.R.id.home) {
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
            return super.onOptionsItemSelected(item)
        }
    }

    /**
     * About Settings
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    class AboutPreferenceFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_about)
            setHasOptionsMenu(true)
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            val id = item.itemId
            if (id == android.R.id.home) {
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
            return super.onOptionsItemSelected(item)
        }
    }

    /**
     * Security Settings
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    class SecurityPreferenceFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_security)
            setHasOptionsMenu(true)
            findPreference(PreferenceConstants.usePasscode).onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, value ->
                if (value as Boolean) {
                    val lockManager = LockManager.getInstance()
                    lockManager.enableAppLock(preference.context, CustomPinLockActivity::class.java)
                    lockManager.appLock.timeout = 1000
                    lockManager.appLock.logoId = R.drawable.ic_memory_black_64dp
                    lockManager.appLock.isFingerprintAuthEnabled = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

                    val intent = Intent(preference.context, CustomPinLockActivity::class.java)
                    intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK)
                    startActivityForResult(intent, 11)
                } else {
                    val lockManager = LockManager.getInstance()
                    lockManager.disableAppLock()
                }
                true
            }
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            val id = item.itemId
            if (id == android.R.id.home) {
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
            return super.onOptionsItemSelected(item)
        }
    }

    companion object {

        /**
         * A preference value change listener that updates the preference's summary
         * to reflect its new value.
         */
        private val sBindPreferenceSummaryToValueListener = Preference.OnPreferenceChangeListener { preference, value ->
            val stringValue = value.toString()

            if (preference is ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                val listPreference = preference
                val index = listPreference.findIndexOfValue(stringValue)

                // Set the summary to reflect the new value.
                preference.setSummary(
                        if (index >= 0)
                            listPreference.entries[index]
                        else
                            null)

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.summary = stringValue
            }
            true
        }

        /**
         * Helper method to determine if the device has an extra-large screen. For
         * example, 10" tablets are extra-large.
         */
        private fun isXLargeTablet(context: Context): Boolean {
            return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_XLARGE
        }

        /**
         * Binds a preference's summary to its value. More specifically, when the
         * preference's value is changed, its summary (line of text below the
         * preference title) is updated to reflect the value. The summary is also
         * immediately updated upon calling this method. The exact display format is
         * dependent on the type of preference.

         * @see .sBindPreferenceSummaryToValueListener
         */
        private fun bindPreferenceSummaryToValue(preference: Preference) {
            // Set the listener to watch for value changes.
            preference.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener

            // Trigger the listener immediately with the preference's
            // current value.
            val prefs = PreferenceHelper.defaultPrefs(preference.context)
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    prefs.getString(preference.key, ""))
        }
    }
}
