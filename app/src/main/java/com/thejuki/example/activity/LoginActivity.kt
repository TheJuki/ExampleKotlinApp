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
import com.thejuki.example.databinding.ActivityLoginBinding
import com.thejuki.example.extension.PreferenceHelper.securePrefs
import com.thejuki.example.extension.PreferenceHelper.set
import com.thejuki.example.extension.simple
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

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
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val prefs = securePrefs(this)

        val serverUrlPref: String? = prefs.getString(PreferenceConstants.serverUrl, "")
        binding.serverUrl.setText(serverUrlPref.orEmpty())

        binding.login.setOnClickListener { login() }
        binding.password.setOnEditorActionListener { _, actionId, _ ->
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

        binding.login.isEnabled = false
        binding.progressbar.visibility = View.VISIBLE

        val simpleAlert = AlertDialog.Builder(this).create()

        val prefs = securePrefs(this)

        val oldServerUrl: String? = prefs.getString(PreferenceConstants.serverUrl, "")
        if (oldServerUrl.orEmpty() != binding.serverUrl.text.toString()) {
            ApiClient.getInstance(this).replaceUrl(binding.serverUrl.text.toString() + "/")
            prefs[PreferenceConstants.serverUrl] = binding.serverUrl.text.toString() + "/"
        }

        if (!ApiClient.getInstance(this).isReady()) {
            binding.progressbar.visibility = View.INVISIBLE
            binding.login.isEnabled = true
            simpleAlert.simple(R.string.server_error_title, R.string.server_error_description)
        } else {
            Log.v("LOGIN START", binding.username.text.toString())
            disposable = ApiClient.getInstance(this).postLogin(binding.username.text.toString(), binding.password.text.toString())
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
                                    binding.progressbar.visibility = View.INVISIBLE

                                    // Update roles
                                    AuthManager.getInstance(this).refresh()

                                    val intent = Intent(this, DrawerActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    binding.progressbar.visibility = View.INVISIBLE
                                    binding.login.isEnabled = true
                                    simpleAlert.simple(R.string.login_error_title, R.string.login_error_description)
                                }
                            },
                            { error ->
                                binding.progressbar.visibility = View.INVISIBLE
                                binding.login.isEnabled = true
                                Log.e("LoginActivity", error.message ?: "")
                                simpleAlert.simple(R.string.server_error_title, R.string.server_error_description)
                            }
                    )
        }
    }

    private fun validate(): Boolean {
        var valid = true

        if (!URLUtil.isNetworkUrl(binding.serverUrl.text.toString().toLowerCase())) {
            binding.serverUrl.error = "Invalid Server URL"
            valid = false
        } else {
            binding.serverUrl.error = null
        }

        if (binding.username.text.isEmpty()) {
            binding.username.error = "Username required"
            valid = false
        } else {
            binding.username.error = null
        }

        if (binding.password.text.isEmpty()) {
            binding.password.error = "Password required"
            valid = false
        } else {
            binding.password.error = null
        }

        return valid
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }
}
