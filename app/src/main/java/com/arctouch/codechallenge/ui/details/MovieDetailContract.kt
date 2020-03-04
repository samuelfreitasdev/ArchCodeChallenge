package com.arctouch.codechallenge.ui.details

import com.arctouch.codechallenge.repository.Movie

interface MovieDetailContract {

	interface View {
		fun showMovieDetail(movie: Movie)
		fun showLoadingIndicator(show: Boolean)
	}

	interface Presenter {
		fun loadMovie(movieId: Long)
		fun destroy()
	}

}