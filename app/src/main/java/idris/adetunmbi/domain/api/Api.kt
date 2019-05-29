package idris.adetunmbi.domain.api

import idris.adetunmbi.features.moviedetail.FavoriteMovieRequestBody
import idris.adetunmbi.features.moviedetail.MovieResponse
import idris.adetunmbi.features.moviesdiscovery.MoviesListResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*


interface Api {
    @GET("discover/movie")
    fun getMovies(@Query("page") page: Int = 1): Single<MoviesListResponse>

    @GET("search/movie")
    fun searchMovies(@Query("query") query: String, @Query("page") page: Int = 1): Single<MoviesListResponse>

    @GET("movie/{movieId}")
    fun getMovie(@Path("movieId") movieId: Int): Single<MovieResponse>

    @POST("account/{}/favorite")
    fun favoriteMovie(@Body body: FavoriteMovieRequestBody): Completable

    @GET("account/{}/favorite/movies")
    fun getFavoriteMovies(): Single<MoviesListResponse>
}
