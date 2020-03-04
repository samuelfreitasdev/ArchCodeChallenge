package com.arctouch.codechallenge.ui.details

import com.arctouch.codechallenge.repository.api.TmdbDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDetailPresenter constructor(
		private val view: MovieDetailContract.View
) : MovieDetailContract.Presenter {

	private val compositeDisposable = CompositeDisposable()
	private val dataSource: TmdbDataSource = TmdbDataSource()

	override fun loadMovie(movieId: Long) {
		view.showLoadingIndicator(true)

		dataSource.movie(movieId)
				.subscribeOn(Schedulers.io())
//				.map { movie -> movie.copy(genres = movie.genreIds?.map { Cache.genres.getValue(it) }) }
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe {
					view.showMovieDetail(it)
					view.showLoadingIndicator(false)
				}
				.let { compositeDisposable.add(it) }
	}

	override fun destroy() {
		compositeDisposable.dispose()
	}

}
