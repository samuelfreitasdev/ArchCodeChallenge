package com.arctouch.codechallenge.ui.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.repository.Movie
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.movie_detail_activity.*
import kotlinx.android.synthetic.main.movie_detail_include.*


class MovieDetailActivity : AppCompatActivity(), MovieDetailContract.View {

	private val presenter = MovieDetailPresenter(this)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.movie_detail_activity)

		toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_18dp)
		toolbar.setNavigationOnClickListener { finish() }

		presenter.loadMovie(getMovieId())
	}

	override fun onDestroy() {
		presenter.destroy()
		super.onDestroy()
	}

	private fun getMovieId() = intent.getLongExtra(PARAM1, -1)

	override fun showMovieDetail(movie: Movie) {

		tvOverview.text = movie.overview
		tvReleaseDate.text = movie.releaseDate

		tvGeners.text = movie.genres.orEmpty()
				.map { it.name }
				.joinToString(", ", "", "") { it }

		toolbar.title = movie.title

		val movieImageUrlBuilder = MovieImageUrlBuilder()

		movie.backdropPath
				?.run { movieImageUrlBuilder.buildBackdropUrl(this) }
				.run { Glide.with(this@MovieDetailActivity).load(this).into(imHeader) }

		movie.posterPath
				?.run { movieImageUrlBuilder.buildPosterUrl(this) }
				.run { Glide.with(this@MovieDetailActivity).load(this).into(imBackdrop) }

	}

	override fun showLoadingIndicator(show: Boolean) {
		if (show) {
			pbLoading.visibility = View.VISIBLE
		} else {
			pbLoading.visibility = View.GONE
		}
	}

	companion object {
		const val PARAM1 = "PARAM1";

		fun newInstance(activity: Activity, movieId: Long) {
			Intent(activity, MovieDetailActivity::class.java)
					.apply { this.putExtra(PARAM1, movieId) }
					.let { activity.startActivity(it) }
		}
	}
}
