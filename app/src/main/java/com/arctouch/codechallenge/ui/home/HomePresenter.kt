package com.arctouch.codechallenge.ui.home

import com.arctouch.codechallenge.repository.Movie
import com.arctouch.codechallenge.repository.api.TmdbDataSource
import com.arctouch.codechallenge.repository.data.Cache
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong

class HomePresenter(private val view: HomeContract.View) : HomeContract.Presenter {

	private val compositeDisposable = CompositeDisposable()
	private val dataSource: TmdbDataSource = TmdbDataSource()

	private val currentPage = AtomicLong(0)

	private val isLoading = AtomicBoolean(false)
	private val hasLoadedAll = AtomicBoolean(false)

	override fun start() {
		view.showLoadingIndicator(true)

		dataSource.genres()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe {
					Cache.cacheGenres(it.genres)
					loadNextPage()
				}
				.let { compositeDisposable.add(it) }
	}

	override fun destroy() {
		compositeDisposable.dispose()
	}

	override fun loadNextPage() {
		setLoading(true)

		dataSource.upcomingMovies(currentPage.incrementAndGet())
				.subscribeOn(Schedulers.io())
				.map { it.results }
				.flatMap { Observable.fromIterable(it) }
				.filter { it.backdropPath.orEmpty().isNotEmpty() }
				.filter { it.posterPath.orEmpty().isNotEmpty() }
				.map { movie -> movie.copy(genres = Cache.genres.values.filter { movie.genreIds?.contains(it.id) == true }) }
				.toList()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe { movies ->
					view.showMovies(movies)
					setLoading(false)
					verifyHasLoadedAllContent(movies)
				}
				.let { compositeDisposable.add(it) }
	}

	private fun setLoading(loading: Boolean) {
		view.showLoadingIndicator(loading)
		isLoading.set(loading)
	}

	private fun verifyHasLoadedAllContent(content: List<Movie>) {
		if (content.isEmpty()) {
			hasLoadedAll.set(true)
		}
	}

	override fun shouldLoadNextPage() = !isLoading.get() && !hasLoadedAll.get()

	override fun clickMovieDetail(movie: Movie) {
		view.goToMovieDetail(movie)
	}


}