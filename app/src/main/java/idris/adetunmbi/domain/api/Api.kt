package idris.adetunmbi.domain.api

import idris.adetunmbi.features.moviesdiscovery.MoviesListResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface Api {
    @GET("discover/movie")
    fun getMovies(@Query("page") page: Int = 1): Single<MoviesListResponse>
}