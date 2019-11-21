package net.purpleocean.themovieapp.data

class MovieListReponse (
    var page: Int,
    val results: List<MovieDetailResponse>
)

class MovieDetailResponse(
    val vote_count: Int,
    val vote_average: Float,
    val title: String,
    val release_date: String,
    val poster_path: String,
    val overview: String?
)