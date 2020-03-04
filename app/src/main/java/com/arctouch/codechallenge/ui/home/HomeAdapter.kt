package com.arctouch.codechallenge.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.repository.Movie
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.movie_item.view.*


class HomeAdapter : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

	private val dataSet = mutableListOf<Movie>()

	class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

		private val movieImageUrlBuilder = MovieImageUrlBuilder()

		fun bind(movie: Movie) {
			itemView.titleTextView.text = movie.title
			itemView.genresTextView.text = movie.genres?.joinToString(separator = ", ") { it.name }
			itemView.releaseDateTextView.text = movie.releaseDate

			Glide.with(itemView)
					.load(movie.posterPath?.let { movieImageUrlBuilder.buildPosterUrl(it) })
					.apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
					.into(itemView.posterImageView)
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
				.let { ViewHolder(it) }
	}

	override fun getItemCount() = dataSet.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(dataSet[position])
	}

	fun addItems(movies: List<Movie>) {
		this.dataSet.addAll(movies)
		notifyDataSetChanged()
	}

	fun getItemByPosition(position: Int): Movie? = dataSet[position]

}
