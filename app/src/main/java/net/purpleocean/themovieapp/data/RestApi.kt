package net.purpleocean.themovieapp.data

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RestApi {
    private val theMovieService: TheMovieService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        theMovieService = retrofit.create(TheMovieService::class.java)
    }

    fun getMovieListRetrofit(param: Map<String, String>): Call<MovieListReponse> {
        return theMovieService.getTop(param)
    }
}