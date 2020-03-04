package com.arctouch.codechallenge.ui.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.repository.Movie
import com.arctouch.codechallenge.ui.details.MovieDetailActivity
import com.arctouch.codechallenge.util.RecyclerItemClickListener
import com.github.yasevich.endlessrecyclerview.EndlessRecyclerView
import kotlinx.android.synthetic.main.home_activity.*


class HomeActivity : AppCompatActivity(),
		HomeContract.View, RecyclerItemClickListener.OnItemClickListener,
		EndlessRecyclerView.Pager {

	private val homeAdapter = HomeAdapter()
	private val presenter = HomePresenter(this)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.home_activity)

		RecyclerItemClickListener(this, this, null)
				.apply { this.maxDistanceForClick = 50 }
				.let { recyclerView.addOnItemTouchListener(it) }

		recyclerView.setHasFixedSize(true)
		recyclerView.layoutManager = LinearLayoutManager(this)
		recyclerView.adapter = homeAdapter
		recyclerView.setPager(this)

//		recyclerView.setProgressView(R.layout.item_progress)

		presenter.start()
	}

	override fun onItemClick(view: View?, position: Int) {
		homeAdapter.getItemByPosition(position)?.let {
			presenter.clickMovieDetail(it)
		}
	}

	override fun showMovies(movies: List<Movie>) {
		homeAdapter.addItems(movies)
	}

	override fun goToMovieDetail(movie: Movie) {
		MovieDetailActivity.newInstance(this, movie.id.toLong())
	}

	override fun showLoadingIndicator(show: Boolean) {
		if (show) {
			progressBar.visibility = View.VISIBLE
		} else {
			progressBar.visibility = View.GONE
		}
	}

	override fun loadNextPage() {
		presenter.loadNextPage()
	}

	override fun shouldLoad(): Boolean {
		return presenter.shouldLoadNextPage()
	}

	override fun onDestroy() {
		presenter.destroy()
		super.onDestroy()
	}
}
