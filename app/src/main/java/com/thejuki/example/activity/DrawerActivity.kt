package com.thejuki.example.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.thejuki.example.PreferenceConstants
import com.thejuki.example.R
import com.thejuki.example.api.ApiClient
import com.thejuki.example.api.AuthManager
import com.thejuki.example.extension.PreferenceHelper
import com.thejuki.example.extension.PreferenceHelper.set
import com.thejuki.example.fragment.QueueFragment
import com.thejuki.example.fragment.list.ContactListFragment
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

/**
 * Drawer Activity
 *
 * The "main" activity. Handles the navigation drawer and fragment within the item container.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class DrawerActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var isSearchMenuItemHidden = false
    private var lastNavigationItemId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        // Set User
        nav_view.getHeaderView(0).username.text = ApiClient.getInstance(this).getUsername()
        nav_view.getHeaderView(0).fullName.text = ApiClient.getInstance(this).getFullName()

        nav_view.setNavigationItemSelectedListener(this)

        authCheck(AuthManager.getInstance(this))

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(ARG_NAVIGATION_ITEM_ID)) {
                lastNavigationItemId = savedInstanceState.getInt(ARG_NAVIGATION_ITEM_ID)
            }
            replaceItemContainer(lastNavigationItemId ?: 0)
        } else {
            nav_view.setCheckedItem(R.id.nav_contacts)
            replaceItemContainer(R.id.nav_contacts)
        }
    }

    private fun authCheck(authManager: AuthManager) {
        (0 until nav_view.menu.size())
                .map { nav_view.menu.getItem(it) }
                .forEach {

                }

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val searchMenuItem = menu.findItem(R.id.action_search)

        if (isSearchMenuItemHidden) {
            searchMenuItem.isVisible = false
            return true
        }

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = searchMenuItem.actionView as SearchView
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(q: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(q: String): Boolean {
                handleSearchQueryChange(q)
                return false
            }
        })

        searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                handleSearchQueryChange(isSearching = false)
                return true
            }

            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }
        })

        return true
    }

    private fun handleSearchQueryChange(q: String? = null, isSearching: Boolean = true) {
        val menuItemId: Int? = (0 until nav_view.menu.size())
                .map { nav_view.menu.getItem(it) }
                .firstOrNull { it.isChecked }
                ?.itemId

        menuItemId?.let { replaceItemContainer(it, q, isSearching) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        replaceItemContainer(item.itemId)
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showHideSearchMenuItem(isHidden: Boolean) {
        if (isSearchMenuItemHidden != isHidden) {
            isSearchMenuItemHidden = isHidden
            invalidateOptionsMenu()
        }
    }

    private fun replaceItemContainer(itemId: Int,
                                     searchTerm: String? = null, isSearching: Boolean = false) {
        var fragment: androidx.fragment.app.Fragment? = null
        when (itemId) {
            R.id.nav_contacts -> {
                title = getString(R.string.contacts)
                showHideSearchMenuItem(false)
                fragment = ContactListFragment.newInstance(
                        searchTerm = searchTerm,
                        isSearching = isSearching)
            }
            R.id.nav_queue -> {
                title = getString(R.string.queue)
                showHideSearchMenuItem(true)
                fragment = QueueFragment.newInstance()
            }
            R.id.nav_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                this.startActivity(intent)
            }

            R.id.nav_logout -> {
                logout()
            }
        }

        if (fragment != null) {
            lastNavigationItemId = itemId
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.item_container, fragment)
            ft.commit()
        }
    }

    private fun logout() {
        val confirmAlert = AlertDialog.Builder(this).create()
        confirmAlert.setTitle(this.getString(R.string.login_confirm_title))
        confirmAlert.setButton(AlertDialog.BUTTON_POSITIVE, this.getString(android.R.string.ok)) { _, _ ->
            val prefs = PreferenceHelper.securePrefs(this)
            prefs[PreferenceConstants.username] = null
            prefs[PreferenceConstants.fullname] = null
            prefs[PreferenceConstants.userContactId] = null
            prefs[PreferenceConstants.token] = null
            prefs[PreferenceConstants.loggedIn] = false
            prefs[PreferenceConstants.userPreferencesData] = null
            prefs[PreferenceConstants.rolesData] = HashSet<String>()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()

        }
        confirmAlert.setButton(AlertDialog.BUTTON_NEGATIVE, this.getString(android.R.string.cancel)) { _, _ ->
        }
        confirmAlert.show()
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(ARG_NAVIGATION_ITEM_ID, lastNavigationItemId ?: 0)
        super.onSaveInstanceState(outState)
    }

    companion object {
        const val ARG_NAVIGATION_ITEM_ID = "navItemId"
    }
}
