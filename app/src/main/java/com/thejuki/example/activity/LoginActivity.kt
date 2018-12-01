package com.thejuki.example.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.thejuki.example.PreferenceConstants
import com.thejuki.example.R
import com.thejuki.example.api.ApiClient
import com.thejuki.example.api.AuthManager
import com.thejuki.example.extension.PreferenceHelper.securePrefs
import com.thejuki.example.extension.PreferenceHelper.set
import com.thejuki.example.extension.simple
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Login Activity
 *
 * Login to the application.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class LoginActivity : AppCompatActivity() {

    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val prefs = securePrefs(this)

        val serverUrlPref: String? = prefs.getString(PreferenceConstants.serverUrl, "")
        serverUrl!!.setText(serverUrlPref.orEmpty())

        login!!.setOnClickListener { login() }
        password!!.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login()
                true
            } else {
                false
            }
        }
    }

    private fun login() {
        if (!validate()) return

        login!!.isEnabled = false
        progressbar.visibility = View.VISIBLE

        val simpleAlert = AlertDialog.Builder(this).create()

        val prefs = securePrefs(this)

        val oldServerUrl: String? = prefs.getString(PreferenceConstants.serverUrl, "")
        if (oldServerUrl.orEmpty() != serverUrl.text.toString()) {
            ApiClient.getInstance(this).replaceUrl(serverUrl.text.toString() + "/")
            prefs[PreferenceConstants.serverUrl] = serverUrl.text.toString() + "/"
        }

        if (!ApiClient.getInstance(this).isReady()) {
            progressbar.visibility = View.INVISIBLE
            login!!.isEnabled = true
            simpleAlert.simple(R.string.server_error_title, R.string.server_error_description)
        } else {
            Log.v("LOGIN START", username.text.toString())
            disposable = ApiClient.getInstance(this).postLogin(username.text.toString(), password.text.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { result ->
                                if (result.Success == true) {
                                    prefs[PreferenceConstants.username] = result.username
                                    prefs[PreferenceConstants.fullname] = result.fullName
                                    prefs[PreferenceConstants.userContactId] = result.id
                                    prefs[PreferenceConstants.token] = result.token
                                    prefs[PreferenceConstants.loggedIn] = true
                                    prefs[PreferenceConstants.rememberUsername] = true
                                    prefs[PreferenceConstants.userPreferencesData] = result.userPreferences
                                    prefs[PreferenceConstants.rolesData] = result.roles
                                    progressbar.visibility = View.INVISIBLE

                                    // Update roles
                                    AuthManager.getInstance(this).refresh()

                                    val intent = Intent(this, DrawerActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    progressbar.visibility = View.INVISIBLE
                                    login!!.isEnabled = true
                                    simpleAlert.simple(R.string.login_error_title, R.string.login_error_description)
                                }
                            },
                            { error ->
                                progressbar.visibility = View.INVISIBLE
                                login!!.isEnabled = true
                                Log.e("LoginActivity", error.message)
                                simpleAlert.simple(R.string.server_error_title, R.string.server_error_description)
                            }
                    )
        }
    }

    private fun validate(): Boolean {
        var valid = true

        if (!URLUtil.isNetworkUrl(serverUrl.text.toString().toLowerCase())) {
            serverUrl!!.error = "Invalid Server URL"
            valid = false
        } else {
            serverUrl!!.error = null
        }

        if (username.text.isEmpty()) {
            username!!.error = "Username required"
            valid = false
        } else {
            username!!.error = null
        }

        if (password.text.isEmpty()) {
            password!!.error = "Password required"
            valid = false
        } else {
            password!!.error = null
        }

        return valid
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }
}
