package com.example.libermovies.activities

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.libermovies.Movie
import com.example.libermovies.R
import com.example.libermovies.adapters.MovieListAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createMoviesRecyclerView()
    }

    private fun createMoviesRecyclerView() {
        val recyclerView = movies_rv
        val moviesAdapter = MovieListAdapter(createExampleMovieList(), this)

        moviesAdapter.onItemClick = { moviePosition:Int ->
            gotoDetailsActivity(moviePosition)
        }

        moviesAdapter.onFavoriteClick = { moviePosition:Int ->
            addFavoriteMovie(moviePosition)
        }

        recyclerView.adapter = moviesAdapter

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
    }

    private fun addFavoriteMovie(moviePosition: Int) {
        println(moviePosition)
    }

    private fun gotoDetailsActivity(moviePosition: Int) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(DetailsActivity.MOVIE_POSITION, moviePosition)
        startActivity(intent)
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

    // Create Dummy list of Movies
    private fun createExampleMovieList(): List<Movie> {
        return listOf(
            Movie("Batman Begins",
                2005,9.5f,
                "https://m.media-amazon.com/images/M/MV5BZmUwNGU2ZmItMmRiNC00MjhlLTg5YWUtODMyNzkxODYzMmZlXkEyXkFqcGdeQXVyNTIzOTk5ODM@._V1_SX300.jpg", false),
            Movie("Batman v Superman: Dawn of Justice",
                2016, 6.2f, "https://m.media-amazon.com/images/M/MV5BYThjYzcyYzItNTVjNy00NDk0LTgwMWQtYjMwNmNlNWJhMzMyXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg", true),
            Movie("Batman Returns",
                1992, 7.1f,"https://m.media-amazon.com/images/M/MV5BOGZmYzVkMmItM2NiOS00MDI3LWI4ZWQtMTg0YWZkODRkMmViXkEyXkFqcGdeQXVyODY0NzcxNw@@._V1_SX300.jpg", false)
        )
    }


}
