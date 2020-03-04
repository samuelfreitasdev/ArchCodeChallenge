package com.arctouch.codechallenge.repository.api

import com.arctouch.codechallenge.repository.GenreResponse
import com.arctouch.codechallenge.repository.Movie
import com.arctouch.codechallenge.repository.UpcomingMoviesResponse
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

class TmdbDataSource {

	private val api: TmdbApi = Retrofit.Builder()
			.baseUrl(URL)
			.client(OkHttpClient.Builder().build())
			.addConverterFactory(MoshiConverterFactory.create())
			.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
			.build()
			.create(TmdbApi::class.java)

	fun genres(): Observable<GenreResponse> = api.genres(API_KEY, DEFAULT_LANGUAGE)

	fun upcomingMovies(page: Long): Observable<UpcomingMoviesResponse> = api.upcomingMovies(API_KEY, DEFAULT_LANGUAGE, page, DEFAULT_REGION)

	fun movie(id: Long): Observable<Movie> = api.movie(id, API_KEY, DEFAULT_LANGUAGE)

	companion object {
		const val URL = "https://api.themoviedb.org/3/"
		const val API_KEY = "1f54bd990f1cdfb230adb312546d765d"
		const val DEFAULT_LANGUAGE = "pt-BR"
		const val DEFAULT_REGION = "BR"
	}
}

private interface TmdbApi {

	@GET("genre/movie/list")
	fun genres(
			@Query("api_key") apiKey: String,
			@Query("language") language: String
	): Observable<GenreResponse>

	@GET("movie/upcoming")
	fun upcomingMovies(
			@Query("api_key") apiKey: String,
			@Query("language") language: String,
			@Query("page") page: Long,
			@Query("region") region: String
	): Observable<UpcomingMoviesResponse>

	@GET("movie/{id}")
	fun movie(
			@Path("id") id: Long,
			@Query("api_key") apiKey: String,
			@Query("language") language: String
	): Observable<Movie>
}
