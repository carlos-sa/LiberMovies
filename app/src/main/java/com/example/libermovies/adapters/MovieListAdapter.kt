package com.example.libermovies.adapters

import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import android.view.View
import android.view.ViewGroup
import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.example.libermovies.Movie
import com.example.libermovies.R
import kotlinx.android.synthetic.main.activity_details.view.*
import kotlinx.android.synthetic.main.movie_list_item.view.*
import com.squareup.picasso.Picasso

class MovieListAdapter(private val moviesList: List<Movie>, private val context: Context) : Adapter<MovieListAdapter.MovieViewHolder>() {

    var onItemClick: ((Int) -> Unit)? = null
    var onFavoriteClick: ((Int) -> Unit)? = null

    // Override necessary methods of Custom Adapter
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

    inner class MovieViewHolder(itemView:View) : ViewHolder(itemView) {

        // Set the movie information on item view
        fun bindView(movie: Movie) {

            val titleTV: TextView = itemView.movie_item_title
            val yearTV: TextView = itemView.movie_item_year
            val favoriteIV: ImageView = itemView.movie_item_favorite_iv
            val posterIV: ImageView = itemView.movie_item_poster_iv

            titleTV.text = movie.name
            yearTV.text = movie.year.toString()
            favoriteIV.setImageResource(if(movie.favorite) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline)
            favoriteIV.setOnClickListener {
                onFavoriteClick?.invoke(adapterPosition)
            }

            loadPosterImage(movie.posterUrl, posterIV)

        }

        private fun loadPosterImage(url: String, posterIV: ImageView){
            Picasso.with(context).load(url).into(posterIV)
        }

        // Set on click listener to movie item
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }
        }

    }
}
