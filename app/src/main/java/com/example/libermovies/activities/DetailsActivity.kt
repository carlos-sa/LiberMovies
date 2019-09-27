package com.example.libermovies.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.libermovies.Movie
import com.example.libermovies.MovieDataProvider
import com.example.libermovies.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.*
import org.json.JSONObject

interface DetailsFetchCallback {
    fun onDetailsFetchDone(response: String)
    fun onDetailsFetchFailed()
}

class DetailsActivity : AppCompatActivity(), DetailsFetchCallback {

    companion object {
        const val MOVIE_POSITION = "MOVIE_POSITION_TAG" // Intent Tag
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        // Retrive movie position in movies list
        val moviePosition = intent.getIntExtra(MOVIE_POSITION, -1)

        val movie = MovieDataProvider.moviesList.get(moviePosition)

        // Enable back button and set movie title
        supportActionBar?.let{
            it.setDisplayHomeAsUpEnabled(true)
            it.title = movie.name
        }

        MovieDataProvider.getMoviesDetails(movie.id, this, this)
        Picasso.with(this).load(movie.posterUrl).into(details_poster_iv)

        movie_details_favorite_iv.setImageResource(if(movie.favorite) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline)
        movie_details_favorite_iv.setOnClickListener{
            onFavoriteClicked(moviePosition)
        }

    }

    private fun onFavoriteClicked(moviePosition: Int) {
        MovieDataProvider.setFavoriteMovie(moviePosition)
        movie_details_favorite_iv.setImageResource(
            if(MovieDataProvider.moviesList.get(moviePosition).favorite) R.drawable.ic_heart_filled
            else R.drawable.ic_heart_outline)
    }

    private fun parseJsonAwnser(response: String) {
        val awnserJson = JSONObject(response)

        movie_details_director_tv.text = awnserJson.getString("Director")
        movie_details_release_tv.text = awnserJson.getString("Released")
        movie_details_duration_tv.text = awnserJson.getString("Runtime")
        movie_details_genre_tv.text = awnserJson.getString("Genre")
        movie_details_cast_tv.text = awnserJson.getString("Actors")
        movie_details_plot_tv.text = awnserJson.getString("Plot")
        details_rating.rating = awnserJson.getDouble("imdbRating").toFloat() / 2

        details_layout_cl.visibility = View.VISIBLE
        movie_details_pb.visibility = View.GONE
    }

    // Finish acitivty on back button
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onNavigateUp()
    }

    override fun onDetailsFetchDone(response: String) {
        parseJsonAwnser(response)
    }

    override fun onDetailsFetchFailed() {
        Toast.makeText(this, "Ocorreu um erro ao carregar os detalhes, tente novamente mais tarde", Toast.LENGTH_LONG).show()
        finish()
    }
}
