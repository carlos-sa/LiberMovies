package com.example.libermovies

import com.android.volley.toolbox.Volley
import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.Response
import android.util.Log
import com.example.libermovies.activities.MoviesFetchCallback
import com.example.libermovies.activities.DetailsFetchCallback
import org.json.JSONObject

object MovieDataProvider {

    lateinit var volleyQueue: RequestQueue
    var TAG = "MOVIE_DATA_PROVIDER"
    var moviesList = mutableListOf<Movie>()
    var favoriteMoviesList = mutableListOf<Movie>()

    fun initVolleyQueue(context: Context) {
        volleyQueue = Volley.newRequestQueue(context)
    }

    fun searchMovies(query: String, context: Context, searchDoneInterface: MoviesFetchCallback) {

        moviesList.clear()

        val url = "http://www.omdbapi.com/?apiKey=" + context.getString(R.string.api_key) +
                "&s=" + query

        val stringRequest = StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->
                println(response)
                if (parseJsonAwnser(response))
                    searchDoneInterface.onSearchDone(query)
                else
                    searchDoneInterface.onSearchFailed()
            },
            Response.ErrorListener { response ->
                Log.e(TAG, "Error" + response)
                searchDoneInterface.onSearchFailed()
            })

        volleyQueue.add(stringRequest)
    }

    fun getMoviesDetails(id: String, context: Context, detailsFetchCallback: DetailsFetchCallback) {
        val url = "http://www.omdbapi.com/?apiKey=" + context.getString(R.string.api_key) +
                "&i=" + id + "&plot=full"

        val stringRequest = StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->
                detailsFetchCallback.onDetailsFetchDone(response)
                println(response)
            },
            Response.ErrorListener { response ->
                Log.e(TAG, "Error" + response)
                detailsFetchCallback.onDetailsFetchFailed()
            })

        volleyQueue.add(stringRequest)
    }

    fun setFavoriteMovie(moviePosition: Int) {
        val movie: Movie
        if (!moviesList.isEmpty())
            movie = moviesList[moviePosition]
        else
            movie = favoriteMoviesList[moviePosition]
        movie.favorite = !movie.favorite
        if (movie.favorite)
            favoriteMoviesList.add(movie)
        else
            favoriteMoviesList.remove(movie)
    }

    private fun findMovieById(movieId: String): Movie? {
        for (i in 0 until favoriteMoviesList.size) {
            if (movieId == favoriteMoviesList[i].id)
                return favoriteMoviesList[i]
        }
        return null
    }

    private fun parseJsonAwnser(response: String): Boolean {
        val awnserJson = JSONObject(response)
        val awnserResponse = awnserJson.getBoolean(response)

        if (!awnserResponse)
            return false

        val movieArray = awnserJson.getJSONArray("Search")
        for (i in 0 until movieArray!!.length()) {
            val movie = findMovieById(movieArray.getJSONObject(i).getString("imdbID"))
            if (movie == null) {
                val newMovie = Movie(
                    movieArray.getJSONObject(i).getString("imdbID"),
                    movieArray.getJSONObject(i).getString("Title"),
                    movieArray.getJSONObject(i).getString("Year"),
                    movieArray.getJSONObject(i).getString("Poster"),
                    false)
                moviesList.add(newMovie)
            } else
                moviesList.add(movie)

        }
        return true
    }
}