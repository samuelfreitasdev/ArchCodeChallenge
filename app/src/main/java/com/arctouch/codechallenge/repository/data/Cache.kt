package com.arctouch.codechallenge.repository.data

import com.arctouch.codechallenge.repository.Genre

object Cache {

	var genres = mapOf<Int, Genre>()

	fun cacheGenres(genres: List<Genre>) {
		this.genres = genres.map { it.id to it }.toMap()
	}

}
