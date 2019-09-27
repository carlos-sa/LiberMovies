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

    fun initVolleyQueue(context: Context) {
        volleyQueue = Volley.newRequestQueue(context)
    }

    fun searchMovies(query: String, context: Context, searchDoneInterface: MoviesFetchCallback) {

        val url = "http://www.omdbapi.com/?apiKey=" + context.getString(R.string.api_key) +
                "&s=" + query

        val stringRequest = StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->
                parseJsonAwnser(response)
                searchDoneInterface.onSearchDone()
                println(response)
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


    private fun parseJsonAwnser(response: String) {
        val awnserJson = JSONObject(response)
        val movieArray = awnserJson.getJSONArray("Search")
        for (i in 0 until movieArray!!.length()) {
            val newMovie = Movie(
                movieArray.getJSONObject(i).getString("imdbID"),
                movieArray.getJSONObject(i).getString("Title"),
                movieArray.getJSONObject(i).getString("Year"),
                movieArray.getJSONObject(i).getString("Poster"),
                false)

            moviesList.add(newMovie)
        }
    }
}