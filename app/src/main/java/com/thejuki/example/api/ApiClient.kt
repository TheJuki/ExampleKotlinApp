package com.thejuki.example.api

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.webkit.URLUtil
import com.thejuki.example.PreferenceConstants
import com.thejuki.example.extension.PreferenceHelper
import com.thejuki.example.json.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * API Client
 *
 * Handles the API calls using Retrofit.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class ApiClient private constructor(context: Context) {
    private var restService: RestService? = null
    private var prefs: SharedPreferences? = null

    init {
        prefs = PreferenceHelper.securePrefs(context)
        val serverUrl: String? = prefs?.getString(PreferenceConstants.serverUrl, "")

        replaceUrl(serverUrl.orEmpty())
    }

    companion object : SingletonHolder<ApiClient, Context>(::ApiClient)

    fun replaceUrl(serverUrl: String) {
        if (URLUtil.isNetworkUrl(serverUrl.toLowerCase())) {
            try {
                val retrofit: Retrofit = Retrofit.Builder()
                        .baseUrl(serverUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build()

                restService = retrofit.create(RestService::class.java)
            } catch (e: IllegalArgumentException) {
                restService = null
                Log.e("ApiClient", e.message ?: "")
            }
        }
    }

    private fun getToken(): String {
        return prefs?.getString(PreferenceConstants.token, "") ?: ""
    }

    fun getUserId(): String {
        return prefs?.getString(PreferenceConstants.userContactId, "") ?: ""
    }

    fun getUsername(): String {
        return prefs?.getString(PreferenceConstants.username, "") ?: ""
    }

    fun getFullName(): String {
        return prefs?.getString(PreferenceConstants.fullname, "") ?: ""
    }

    // Check if Retrofit created the RestService
    fun isReady(): Boolean {
        return this.restService != null
    }

    // Login
    fun postLogin(username: String, password: String): Observable<UserJson> {
        return restService!!.postLogin(username, password)
    }

    // Register device ID for notifications
    private fun postDeviceRegistration(deviceToken: String): Observable<SuccessStatusJson> {
        return restService!!.postDeviceRegistration(getToken(), deviceToken, "Android")
    }

    // Update device ID
    @SuppressLint("CheckResult")
    fun updateDeviceRegistration(deviceToken: String) {
        postDeviceRegistration(deviceToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            if (result.status == "Success") {
                                Log.i("updateDeviceReg", "Device ID Registration succeeded")
                            } else {
                                Log.e("updateDeviceReg", "Error: Device ID Registration failed")
                            }
                        },
                        { error ->
                            Log.e("updateDeviceReg", error.message ?: "")
                        }
                )
    }

    //region Contact API

    // Get contacts by search term
    fun getContacts(term: String): Observable<List<TableCellJson>> {
        return restService!!.getContacts(getToken(), term)
    }

    fun getContact(id: String): Observable<ContactJson> {
        return restService!!.getContact(getToken(), id)
    }

    fun postContact(json: ContactJson): Observable<ContactJson> {
        json.modifyingUser = getUsername()
        return restService!!.postContact(getToken(), json)
    }

    fun getAutoCompleteContacts(term: String): ArrayList<ContactLookupJson> {
        return restService!!.getAutoCompleteContacts(getToken(), term)
                .blockingFirst(null)
    }
    //endregion

    //region Note API
    fun getNotes(pId: String, pType: String, type: String): Observable<List<TableCellJson>> {
        return restService!!.getNotes(getToken(), pId, pType, type)
    }

    fun getNote(id: String): Observable<NoteJson> {
        return restService!!.getNote(getToken(), id)
    }

    fun postNote(json: NoteJson): Observable<NoteJson> {
        json.modifyingUser = getUsername()
        return restService!!.postNote(getToken(), json)
    }
    //endregion

    //region Team API
    fun getTeams(): Observable<ArrayList<ListItemJson>> {
        return restService!!.getTeams(getToken())
    }
    //endregion

    //region Counts API
    fun getCounts(): Observable<CountsJson> {
        return restService!!.getCounts(getToken())
    }
    //endregion
}
