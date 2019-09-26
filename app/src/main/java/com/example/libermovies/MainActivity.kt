package com.example.libermovies

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // Create menu items
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    // Handle Menu items click
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.search_button -> {
                onSearchButton()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Search Button handler fuction, calls search widget
    private fun onSearchButton() {
        println("On Search Requested")
        onSearchRequested()
    }

    // Receive new intents to this activity
    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        handleIntent(intent)
    }

    // Deal with received intents
    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            doSearch(intent.getStringExtra(SearchManager.QUERY))
        }
    }

    // Handle search queries
    private fun doSearch(query: String) {
        // TODO: Insert here code to search on omdbapi
    }

}
