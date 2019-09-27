package com.example.libermovies.activities

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.libermovies.Movie
import com.example.libermovies.MovieDataProvider
import com.example.libermovies.R
import com.example.libermovies.adapters.MovieListAdapter
import kotlinx.android.synthetic.main.activity_main.*

interface MoviesFetchCallback {
    fun onSearchDone(query: String)
    fun onSearchFailed()
}

class MainActivity : AppCompatActivity(), MoviesFetchCallback {

    lateinit var moviesAdapter: MovieListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MovieDataProvider.initVolleyQueue(this)

    }

    private fun createMoviesRecyclerView(moviesList: List<Movie>) {
        val recyclerView = movies_rv
        moviesAdapter = MovieListAdapter(moviesList, this)

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
        MovieDataProvider.setFavoriteMovie(moviePosition)
        moviesAdapter.notifyDataSetChanged()
        if (MovieDataProvider.moviesList.isEmpty() &&
                MovieDataProvider.favoriteMoviesList.isEmpty())
            showEmptyState()
        println(moviePosition)
    }

    private fun gotoDetailsActivity(moviePosition: Int) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(DetailsActivity.MOVIE_POSITION, moviePosition)
        startActivity(intent)
    }

    override fun onResume() {
        if (::moviesAdapter.isInitialized) moviesAdapter.notifyDataSetChanged()

        super.onResume()
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
            R.id.clear_button -> {
                onClearButton()
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

    // Clear Button handler, clears movie result list
    private fun onClearButton() {
        MovieDataProvider.moviesList.clear()
        if (MovieDataProvider.favoriteMoviesList.isEmpty())
            showEmptyState()
        else {
            showMovieList()
            createMoviesRecyclerView(MovieDataProvider.favoriteMoviesList)
            movie_list_title.text = "Seus filmes favoritos"
        }
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
        showProgressBar()
        MovieDataProvider.searchMovies(query, this, this)
    }

    private fun showProgressBar() {
        movies_ll.visibility = View.GONE
        empty_state_tv.visibility = View.GONE
        movie_search_pb.visibility = View.VISIBLE
    }

    private fun showEmptyState() {
        movies_ll.visibility = View.GONE
        empty_state_tv.visibility = View.VISIBLE
        movie_search_pb.visibility = View.GONE
    }

    private fun showMovieList() {
        movies_ll.visibility = View.VISIBLE
        empty_state_tv.visibility = View.GONE
        movie_search_pb.visibility = View.GONE
    }

    override fun onSearchDone(query: String) {
        createMoviesRecyclerView(MovieDataProvider.moviesList)
        movie_list_title.text = "Exibindo resultados: $query"
        showMovieList()
    }

    override fun onSearchFailed() {
        showEmptyState()
        Toast.makeText(this, "Ocorreu um erro ao pesquisar, tente novamente mais tarde", Toast.LENGTH_LONG).show()
    }

}
