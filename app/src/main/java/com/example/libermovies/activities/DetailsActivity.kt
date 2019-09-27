package com.example.libermovies.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.libermovies.Movie
import com.example.libermovies.R

class DetailsActivity : AppCompatActivity() {

    companion object {
        const val MOVIE_POSITION = "MOVIE_POSITION_TAG" // Intent Tag
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        // Create fake movie for mocking
        val movie = Movie("Batman Begins",
            2005,9.5f,
            "https://m.media-amazon.com/images/M/MV5BZmUwNGU2ZmItMmRiNC00MjhlLTg5YWUtODMyNzkxODYzMmZlXkEyXkFqcGdeQXVyNTIzOTk5ODM@._V1_SX300.jpg", false)

        // Retrive movie position in movies list
        val moviePosition = intent.getIntExtra(MOVIE_POSITION, -1)

        // Enable back button and set movie title
        supportActionBar?.let{
            it.setDisplayHomeAsUpEnabled(true)
            it.title = movie.name
        }
    }

    // Finish acitivty on back button
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onNavigateUp()
    }
}
