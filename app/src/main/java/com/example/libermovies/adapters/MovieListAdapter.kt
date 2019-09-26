package com.example.libermovies.adapters

import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import android.view.View
import android.view.ViewGroup
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import com.example.libermovies.Movie
import com.example.libermovies.R
import kotlinx.android.synthetic.main.movie_list_item.view.*

class MovieListAdapter(private val moviesList: List<Movie>, private val context: Context) : Adapter<MovieListAdapter.MovieViewHolder>() {

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindView(moviesList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.movie_list_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    class MovieViewHolder(itemView:View) : ViewHolder(itemView) {

        fun bindView(movie: Movie) {

            val title: TextView = itemView.movie_item_title
            val year: TextView = itemView.movie_item_year

            title.text = movie.name
            year.text = movie.year.toString()
        }
    }
}
