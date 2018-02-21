package com.thejuki.example.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle

/**
 * Search Results Activity
 *
 * This activity is used when the search icon is invoked on the [DrawerActivity].
 * I handle the query in [DrawerActivity] which I then send to the current fragment in the item container.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class SearchResultsActivity : Activity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        //handleIntent(intent)
    }

    /**
     * Query text handled in [DrawerActivity]
     */
    private fun handleIntent(intent: Intent) {

        if (Intent.ACTION_SEARCH == intent.action) {
            // val query = intent.getStringExtra(SearchManager.QUERY)
        }
    }
}
