package com.arctouch.codechallenge.ui.home

import com.arctouch.codechallenge.repository.Movie

interface HomeContract {
	interface View {
		fun showMovies(movies: List<Movie>)
		fun showLoadingIndicator(show: Boolean)
		fun goToMovieDetail(movie: Movie)

		fun loadNextPage()
		fun shouldLoad() : Boolean
	}

	interface Presenter {
		fun start()
		fun destroy()
		fun clickMovieDetail(movie: Movie)
		fun shouldLoadNextPage(): Boolean
		fun loadNextPage()
	}
}