package net.purpleocean.themovieapp.ui

import io.reactivex.Observable
import net.purpleocean.themovieapp.data.*

class MovieManager(private val api: RestApi = RestApi()) {
    fun getMovieList(page: String): Observable<MovieList> {
        return Observable.create { subscriber ->
            val param = mapOf(
                "page" to page,
                "api_key" to API_KEY,
                "sort_by" to "popularity.desc",
                "language" to "ko"
            )
            val call = api.getMovieListRetrofit(param)
            val response = call.execute()

            if (response.isSuccessful) {
                val movieListResults = response.body()?.results?.map {
                    MovieItem(
                        it.vote_count,
                        it.vote_average,
                        it.title,
                        it.release_date,
                        it.poster_path,
                        it.overview
                    )
                }
                if (movieListResults != null) {
                    var responsePage = response.body()?.page?.plus(1)
                    val movieList = MovieList(responsePage, movieListResults)
                    subscriber.onNext(movieList)
                }
                subscriber.onComplete()
            } else {
                subscriber.onError(Throwable(response.message()))
            }
        }
    }

    suspend fun getMovieList(param: Map<String, String>): MovieList {
        val result = api.getMovieListCo(param)
        return process(result)
    }

    private fun process(response: MovieListResponse): MovieList {
        val list = response.results.map {
            MovieItem(
                it.vote_count,
                it.vote_average,
                it.title,
                it.release_date,
                it.poster_path,
                it.overview
            )
        }
        return MovieList(response.page, list)
    }
}